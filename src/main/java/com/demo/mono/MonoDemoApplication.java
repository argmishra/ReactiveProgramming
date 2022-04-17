package com.demo.mono;

import com.demo.util.Utils;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoDemoApplication {

  public static void main(String[] args) {

    Stream<Integer> integerStream = Stream.of(1).map(i -> i * 2);
    System.out.println("Lazy Stream Demo : " + integerStream);
    integerStream.forEach(i -> System.out.println("Lazy Stream Demo : " + i));

    Mono<Integer> integerMono = Mono.just(1);
    integerMono.subscribe(i -> System.out.println("Mono Demo : " + i));

    System.out.println("********** onNext and onComplete Demo **********");
    Mono<String> stringMono = Mono.just("ball");
    stringMono.subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** Mono empty Demo **********");
    stringMono = Mono.empty();
    stringMono.subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** onError Demo **********");
    integerMono = Mono.just("ball").map(String::length).map(l -> l / 0);
    integerMono.subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** Empty or Error Demo **********");
    userRepo(1).subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** fromSupplier Demo **********");
    stringMono = Mono.fromSupplier(() -> "Demo");
    stringMono.subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** fromCallable Demo **********");
    stringMono = Mono.fromCallable(() -> "Demo");
    stringMono.subscribe(
        Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** fromFuture Demo **********");
    Mono.fromFuture(Utils.getName()).subscribe(Utils.onNext());

    Runnable runnable = () -> System.out.println("********** fromRunnable Demo **********");
    Mono.fromRunnable(runnable).subscribe(Utils.onNext(),
        Utils.onError(),
        Utils.onComplete());

    System.out.println("********** map Demo **********");
    Mono.just("Anurag").map(s -> s.toLowerCase()).subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** flatMap Demo **********");
    Mono.just("Anurag").flatMap(s -> split(s)).subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

  }


  private static  Mono<String> split(String name) {
    return Mono.just(name.concat( " Mishra"));
  }

  private static Mono<String> userRepo(int userId) {
    if (userId == 1) {
      return Mono.just(Utils.faker().name().firstName());
    } else if (userId == 2) {
      return Mono.empty();
    } else {
      return Mono.error(new RuntimeException("Invalid id"));
    }

  }



}
