package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;
    @Mock
    private ReviewService reviewService;

    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies() {
        //given - precondition or setup
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenCallRealMethod();

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMovies_1() {
        //given - precondition or setup
        var errorMessage = "Exception occurred in ReviewService";
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();
    }
}