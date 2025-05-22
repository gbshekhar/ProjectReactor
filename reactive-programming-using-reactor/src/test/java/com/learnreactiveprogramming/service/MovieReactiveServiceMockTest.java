package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;

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
    void getAllMovies_Exception() {
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

    @Test
    void getAllMovies_Exception_Retry() {
        //given - precondition or setup
        var errorMessage = "Exception occurred in ReviewService";
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies_retry();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();

        Mockito.verify(reviewService, Mockito.times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_Exception_RetryWhen() {
        //given - precondition or setup
        var errorMessage = "Exception occurred in ReviewService";
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies_retryWhen();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectError(ServiceException.class)
                .verify();

        Mockito.verify(reviewService, Mockito.times(1))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_Exception_RetryWhen_1() {
        //given - precondition or setup
        var errorMessage = "Exception occurred in ReviewService";
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenThrow(new NetworkException(errorMessage));

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies_retryWhen();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();

        Mockito.verify(reviewService, Mockito.times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_repeat() {
        //given - precondition or setup
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenCallRealMethod();

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies_repeat();

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectNextCount(6)
                .thenCancel()
                .verify();

        Mockito.verify(reviewService, Mockito.times(6))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_repeat_n() {
        //given - precondition or setup
        Mockito.when(movieInfoService.movieInfoFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(ArgumentMatchers.anyLong()))
                .thenCallRealMethod();
        var noOfTimes = 2L;

        //when - action or behaviour we are going to test
        var moviesFlux = movieReactiveService.getAllMovies_repeat_n(noOfTimes);

        //then - verify output
        StepVerifier.create(moviesFlux)
                .expectNextCount(9)
                .verifyComplete();

        Mockito.verify(reviewService, Mockito.times(9))
                .retrieveReviewsFlux(isA(Long.class));
    }
}