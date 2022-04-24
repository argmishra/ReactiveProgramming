package com.demo.pratice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testng.annotations.Test;
import reactor.test.StepVerifier;

public class ReactiveDemoApplicationTest {

  private final MovieInfoService movieInfoService = new MovieInfoService();
  private final ReviewService reviewService = new ReviewService();
  ReactiveDemoApplication reactiveDemoApplication =
      new ReactiveDemoApplication(movieInfoService, reviewService);

  @Test
  public void testGetAllMovies() {
    var moviesFlux = reactiveDemoApplication.getAllMovies();

    StepVerifier.create(moviesFlux).assertNext(movie -> {
      assertEquals(movie.getMovie().getName(), "Batman Begins");
      assertEquals(movie.getReviewList(), 2);
    }).assertNext(movie -> {
      assertEquals(movie.getMovie().getName(), "The Dark Knight");
      assertEquals(movie.getReviewList(), 2);
    });
  }

  @Test
  public void testGetMovieById() {
    var moviesFlux = reactiveDemoApplication.getMovieById(1);

    StepVerifier.create(moviesFlux).assertNext(movie -> {
      assertEquals(movie.getMovie().getName(), "Batman Begins");
      assertEquals(movie.getReviewList(), 2);
    });
  }

  @Test
  public void testGetMovie() {
    var moviesFlux = reactiveDemoApplication.getMovie(1);

    StepVerifier.create(moviesFlux).assertNext(movie -> {
      assertEquals(movie.getMovie().getName(), "Batman Begins");
      assertEquals(movie.getReviewList(), 2);
    });
  }

}
