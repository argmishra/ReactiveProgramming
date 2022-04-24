package com.demo.pratice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactiveDemoApplication {

  private final MovieInfoService movieInfoService;
  private final ReviewService reviewService;

  public ReactiveDemoApplication(MovieInfoService movieInfoService, ReviewService reviewService) {
    this.movieInfoService = movieInfoService;
    this.reviewService = reviewService;
  }

  public Flux<Movie> getAllMovies() {
    var movieInfoFlux = movieInfoService.retrieveMoviesFlux();
    return movieInfoFlux.flatMap(movieInfo -> {
      Mono<List<Review>> review =
          reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
      return review.map(reviews -> new Movie(movieInfo, reviews));
    });
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
