package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    public  MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService){
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Movie> getAllMovies(){
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                }).log();
    }

    public Mono<Movie> getMovieById(Long movieId){
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsList = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(reviewsList, (movieInfo, reviews) -> new Movie(movieId, movieInfo, reviews));
    }
}
