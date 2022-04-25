package com.demo.pratice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.testng.annotations.Test;
import reactor.test.StepVerifier;

public class StreamDemoTests {

  private final MovieInfoService movieInfoService = new MovieInfoService();
  private final ReviewService reviewService = new ReviewService();
  FluxMonoStreamDemo fluxMonoDemoApplication = new FluxMonoStreamDemo();
  ExceptionHandlingStream exceptionHandlingStream = new ExceptionHandlingStream();
  ReactiveDemoApplication reactiveDemoApplication =
      new ReactiveDemoApplication(movieInfoService, reviewService);


  @Test
  public void testNamesFlux() {
    var namesFlux = fluxMonoDemoApplication.namesFlux();

    StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();

    StepVerifier.create(namesFlux).expectNext("alex").expectNextCount(2).verifyComplete();
  }

  @Test
  public void testNameMono() {
    var namesFlux = fluxMonoDemoApplication.nameMono();

    StepVerifier.create(namesFlux).expectNext("alex").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(1).verifyComplete();

    StepVerifier.create(namesFlux).expectNext("alex").expectNextCount(0).verifyComplete();
  }

  @Test
  public void testNamesFlux_map() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_map();

    StepVerifier.create(namesFlux).expectNext("ALEX", "BEN", "CHLOE").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();

    StepVerifier.create(namesFlux).expectNext("ALEX").expectNextCount(2).verifyComplete();
  }

  @Test
  public void testNamesFlux_immutable() {

    var namesFlux = fluxMonoDemoApplication.namesFlux_immutable();

    StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();

    StepVerifier.create(namesFlux).expectNext("alex").expectNextCount(2).verifyComplete();
  }

  @Test
  public void testNamesFlux_map_filter() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_map_filter(3);

    StepVerifier.create(namesFlux).expectNext("4-ALEX", "5-CHLOE").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(2).verifyComplete();

    StepVerifier.create(namesFlux).expectNext("4-ALEX").expectNextCount(1).verifyComplete();
  }

  @Test
  public void testNamesFlux_flatMap_filter() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_flatMap_filter(3);

    StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();
  }

  @Test
  public void testNamesFlux_flatMap_filter_delayElements() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_flatMap_filter_delayElements(3);

    StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();
  }

  @Test
  public void testNamesFlux_contactMap_filter() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_contactMap_filter(3);

    StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();
  }

  @Test
  public void testNameMono_map_flatMap() {
    var namesFlux = fluxMonoDemoApplication.nameMono_map_flatMap(3);

    StepVerifier.create(namesFlux).expectNext(List.of("A", "L", "E", "X")).verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testNameMono_map_flatMapMany() {
    var namesFlux = fluxMonoDemoApplication.nameMono_map_flatMapMany(3);

    StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(4).verifyComplete();
  }

  @Test
  public void testNamesFlux_transform() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_transform(3);

    StepVerifier.create(namesFlux).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();
  }

  @Test
  public void testNamesFlux_defaultIfEmpty() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_defaultIfEmpty(30);

    StepVerifier.create(namesFlux).expectNext("default").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testNamesFlux_switchIfEmpty() {
    var namesFlux = fluxMonoDemoApplication.namesFlux_switchIfEmpty(30);

    StepVerifier.create(namesFlux).expectNext("Switch").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testConcat_flux() {
    var namesFlux = fluxMonoDemoApplication.concat_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void testConcatWith_flux() {
    var namesFlux = fluxMonoDemoApplication.concatWith_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void testMerge_flux() {
    var namesFlux = fluxMonoDemoApplication.merge_flux();

    StepVerifier.create(namesFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void testMergeWith_flux() {
    var namesFlux = fluxMonoDemoApplication.merge_flux();

    StepVerifier.create(namesFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void testMergeSequential_flux() {
    var namesFlux = fluxMonoDemoApplication.mergeSequential_flux();

    StepVerifier.create(namesFlux).expectNextCount(6).verifyComplete();
  }

  @Test
  public void testZip_flux() {
    var namesFlux = fluxMonoDemoApplication.zip_flux();

    StepVerifier.create(namesFlux).expectNext("ADG", "BEH", "CFI").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  public void testZipWith_flux() {
    var namesFlux = fluxMonoDemoApplication.zipWith_flux();

    StepVerifier.create(namesFlux).expectNext("AD", "BE", "CF").verifyComplete();
    StepVerifier.create(namesFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  public void testException_flux() {
    var namesFlux = exceptionHandlingStream.exception_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C").expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void testException_flux1() {
    var namesFlux = exceptionHandlingStream.exception_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C").expectError().verify();
  }

  @Test
  public void testException_flux2() {
    var namesFlux = exceptionHandlingStream.exception_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C").expectErrorMessage("Exception")
        .verify();
  }

  @Test
  public void testOnErrorReturn_flux() {
    var namesFlux = exceptionHandlingStream.onErrorReturn_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "Z", "D", "E", "F").verifyComplete();
  }

  @Test
  public void testOnErrorResume_flux() {
    var namesFlux = exceptionHandlingStream.onErrorResume_flux(new RuntimeException("Exception"));

    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "R", "D", "E", "F").verifyComplete();
  }

  @Test
  public void testOnErrorResume_flux1() {
    var namesFlux =
        exceptionHandlingStream.onErrorResume_flux(new InterruptedException("Exception"));

    StepVerifier.create(namesFlux).expectNext("A", "B", "C")
        .expectError(InterruptedException.class).verify();
  }

  @Test
  public void testOnErrorContinue_flux() {
    var namesFlux = exceptionHandlingStream.onErrorContinue_flux();

    StepVerifier.create(namesFlux).expectNext("B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  public void testOnErrorMap_flux() {
    var namesFlux = exceptionHandlingStream.onErrorMap_flux();

    StepVerifier.create(namesFlux).expectError(ReactorException.class).verify();
  }

  @Test
  public void testDoOnError_flux() {
    var namesFlux = exceptionHandlingStream.doOnError_flux();

    StepVerifier.create(namesFlux).expectNext("A", "B", "C")
        .expectError(Exception.class).verify();
  }

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
