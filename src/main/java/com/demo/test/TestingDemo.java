package com.demo.test;

import com.demo.util.BookOrder;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class TestingDemo {

  @Test
  public void test1() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    StepVerifier.create(just).expectNext(1).expectNext(2).expectNext(3).verifyComplete();
  }

  @Test
  public void test2() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    StepVerifier.create(just).expectNext(1, 2, 3).verifyComplete();
  }

  @Test
  public void test3() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    Flux<Integer> error = Flux.error(new RuntimeException("error"));
    Flux<Integer> concat = Flux.concat(just, error);
    StepVerifier.create(concat).expectNext(1, 2, 3).verifyError();
  }

  @Test
  public void test4() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    Flux<Integer> error = Flux.error(new RuntimeException("error"));
    Flux<Integer> concat = Flux.concat(just, error);
    StepVerifier.create(concat).expectNext(1, 2, 3).verifyError(RuntimeException.class);
  }

  @Test
  public void test5() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    Flux<Integer> error = Flux.error(new RuntimeException("error"));
    Flux<Integer> concat = Flux.concat(just, error);
    StepVerifier.create(concat).expectNext(1, 2, 3).verifyErrorMessage("error");
  }

  @Test
  public void test6() {
    Flux<Integer> range = Flux.range(1, 20);
    StepVerifier.create(range).expectNextCount(20).verifyComplete();
  }

  @Test
  public void test7() {
    Flux<Integer> range = Flux.range(1, 20);
    StepVerifier.create(range).thenConsumeWhile(i -> i < 30).verifyComplete();
  }

  @Test
  public void test8() {
    Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder());
    StepVerifier.create(mono)
        .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
        .verifyComplete();
  }

  @Test
  public void test9() {
    Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder())
        .delayElement(Duration.ofSeconds(2));

    StepVerifier.create(mono)
        .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
        .expectComplete()
        .verify(Duration.ofSeconds(4));
  }

  @Test
  public void test10() {
    Flux<Integer> just = Flux.just(1, 2, 3);
    StepVerifierOptions stepVerifierOptions = StepVerifierOptions.create().scenarioName("TEST NOW");
    StepVerifier.create(just, stepVerifierOptions).expectNext(15).verifyComplete();
  }

}
