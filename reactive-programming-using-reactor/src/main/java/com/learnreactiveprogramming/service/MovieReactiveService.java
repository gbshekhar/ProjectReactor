package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;
    private RevenueService revenueService;

    public  MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService){
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies(){
        //Error Behaviour - Throw a MovieException when on of the calls fails.
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                   log.error("Exception is:", ex);
                   throw new MovieException(ex.getMessage());
                })
                .log();
    }

    //Retry when Exception occurs
    public Flux<Movie> getAllMovies_retry(){
        //Error Behaviour - Throw a MovieException when on of the calls fails.
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is:", ex);
                    throw new MovieException(ex.getMessage());
                })
                .retry(3)
                .log();
    }

    //Retry with backoff, when Exception occurs
    public Flux<Movie> getAllMovies_retryWhen(){
        //Error Behaviour - Throw a MovieException when on of the calls fails.
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is:", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .log();
    }

    private static RetryBackoffSpec getRetryBackoffSpec() {
        return  Retry.backoff(3, Duration.ofMillis(500))
                .filter(ex -> ex instanceof MovieException)//retry on when Movie Exception
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure())));//Throw actual Exception after retry count Exhausted

    }

    //Repeat
    public Flux<Movie> getAllMovies_repeat(){
        //Error Behaviour - Throw a MovieException when on of the calls fails.
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is:", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat()
                .log();
    }

    //Repeat
    public Flux<Movie> getAllMovies_repeat_n(long n){
        //Error Behaviour - Throw a MovieException when on of the calls fails.
        var moviesInfoFlux = movieInfoService.movieInfoFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieId())
                            .collectList();
                    return reviewsMono
                            .map(reviewList -> new Movie(movieInfo.getMovieId(), movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is:", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat(n)
                .log();
    }

    public Mono<Movie> getMovieById(Long movieId){
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsList = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(reviewsList, (movieInfo, reviews) -> new Movie(movieId, movieInfo, reviews));
    }

    public Mono<Movie> getMovieById_withRevenue(Long movieId){
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsList = reviewService.retrieveReviewsFlux(movieId).collectList();
        //Revenue Service is having delay so its a blocking call and also its not returning Reactive type i.e Flux or Mono
        //Making Blocking call as non blocking call
        //Wrapping the response as Mono
        var revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());
        return movieInfoMono.zipWith(reviewsList, (movieInfo, reviews) -> new Movie(movieId, movieInfo, reviews))
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                });
    }
}
