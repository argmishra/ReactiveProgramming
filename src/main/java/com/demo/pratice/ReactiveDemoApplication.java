package com.demo.pratice;

import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Slf4j
public class ReactiveDemoApplication {

  private final MovieInfoService movieInfoService;
  private final ReviewService reviewService;

  public ReactiveDemoApplication(MovieInfoService movieInfoService, ReviewService reviewService) {
    this.movieInfoService = movieInfoService;
    this.reviewService = reviewService;
  }

  private static RetryBackoffSpec getRetryBackoffSpec() {
    return Retry.fixedDelay(2, Duration.ofMillis(500))
        .filter(ex -> ex instanceof UserException)
        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) ->
            Exceptions.propagate(retrySignal.failure())));
  }

  public Flux<Movie> getAllMovies() {
    var movieInfoFlux = movieInfoService.retrieveMoviesFlux();
    return movieInfoFlux.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    }).onErrorMap(throwable -> {
      throw new UserException(throwable.getMessage());
    });
  }

  public Flux<Movie> getAllMovies_retry() {
    var movieInfoFlux = movieInfoService.retrieveMoviesFlux();
    return movieInfoFlux.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    }).onErrorMap(throwable -> {
      throw new UserException(throwable.getMessage());
    }).retry(2);
  }

  public Flux<Movie> getAllMovies_retryWhen() {
    var movieInfoFlux = movieInfoService.retrieveMoviesFlux();

    return movieInfoFlux.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    }).onErrorMap(throwable -> {
      throw new UserException(throwable.getMessage());
    }).retryWhen(getRetryBackoffSpec());
  }

  public Flux<Movie> getAllMovies_repeat() {
    var movieInfoFlux = movieInfoService.retrieveMoviesFlux();

    return movieInfoFlux.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    }).onErrorMap(throwable -> {
      throw new UserException(throwable.getMessage());
    }).retryWhen(getRetryBackoffSpec()).repeat(2);
  }

  public Mono<Movie> getMovieById(long id) {
    var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(id);
    var reviewsFlux = reviewService.retrieveReviewsFlux(id).collectList();
    return movieInfoMono.zipWith(reviewsFlux, (m, r) -> new Movie(m, r));
  }

  public Mono<Movie> getMovie(long id) {
    var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(id);
    return movieInfoMono.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    });
  }

}
