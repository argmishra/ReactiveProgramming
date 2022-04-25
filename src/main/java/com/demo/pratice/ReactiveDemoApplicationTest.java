package com.demo.pratice;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ReactiveDemoApplicationTest {

  @InjectMocks
  ReactiveDemoApplication reactiveMovieService;
  @Mock
  private MovieInfoService movieInfoService;
  @Mock
  private ReviewService reviewService;

  @Test
  void getAllMovies() {
    Mockito.when(movieInfoService.retrieveMoviesFlux())
        .thenCallRealMethod();

    Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenCallRealMethod();

    var moviesFlux = reactiveMovieService.getAllMovies();

    StepVerifier.create(moviesFlux)
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void getAllMovies_exception() {
    Mockito.when(movieInfoService.retrieveMoviesFlux())
        .thenCallRealMethod();

    Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new RuntimeException("Exception"));

    var moviesFlux = reactiveMovieService.getAllMovies();

    StepVerifier.create(moviesFlux)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void getAllMovies_retry() {
    Mockito.when(movieInfoService.retrieveMoviesFlux())
        .thenCallRealMethod();

    Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new RuntimeException("Exception"));

    var moviesFlux = reactiveMovieService.getAllMovies_retry();

    StepVerifier.create(moviesFlux)
        .expectError(RuntimeException.class)
        .verify();

    verify(reviewService, times(3)).retrieveReviewsFlux(isA(Long.class));
  }

  @Test
  void getAllMovies_retryWhen() {
    Mockito.when(movieInfoService.retrieveMoviesFlux())
        .thenCallRealMethod();

    Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenThrow(new RuntimeException("Exception"));

    var moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

    StepVerifier.create(moviesFlux)
        .expectError(RuntimeException.class)
        .verify();

    verify(reviewService, times(3)).retrieveReviewsFlux(isA(Long.class));
  }

  @Test
  void getAllMovies_repeat() {
    Mockito.when(movieInfoService.retrieveMoviesFlux())
        .thenCallRealMethod();

    Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
        .thenCallRealMethod();

    var moviesFlux = reactiveMovieService.getAllMovies_repeat();

    StepVerifier.create(moviesFlux)
        .expectNextCount(9).verifyComplete();

    verify(reviewService, times(9)).retrieveReviewsFlux(isA(Long.class));
  }


}
