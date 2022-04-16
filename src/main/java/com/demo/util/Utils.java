package com.demo.util;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Utils {

  private static final Faker faker = Faker.instance();
  private static final List<String> stringList = new ArrayList<>();

  public static Consumer<Object> onNext() {
    return o -> System.out.println("RECEIVED : " + o);
  }

  public static Consumer<Throwable> onError() {
    return o -> System.out.println("ERROR : " + o.getMessage());
  }

  public static Runnable onComplete() {
    return () -> System.out.println("COMPLETED");
  }

  public static Faker faker() {
    return faker;
  }

  public static CompletableFuture<String> getName() {
    return CompletableFuture.supplyAsync(() -> Utils.faker().name().firstName());
  }

  public static Subscriber<Object> subscriber(String name) {
    return new DefaultSubscriber(name);
  }

  public static Subscriber<Object> subscriber() {
    return new DefaultSubscriber();
  }

  public static Function<Flux<Integer>, Flux<Integer>> userFilter() {
    return f -> f.filter(a -> a > 2).doOnNext(a -> a.compareTo(7));
  }

  public static Flux<String> generateNames() {
    return Flux.generate(stringSynchronousSink -> {
          String name = faker().name().firstName();
          stringList.add(name);
          stringSynchronousSink.next(name);
        }).startWith(getFromCache())
        .cast(String.class);
  }

  public static Flux<String> getFromCache() {
    return Flux.fromIterable(stringList);
  }

  public static Flux<Integer> getIntegers() {
    return Flux.range(1, 3)
        .doOnSubscribe(s -> System.out.println("Subscribed"))
        .doOnComplete(() -> System.out.println("--Completed"));
  }

  public static Flux<Integer> getIntegersWithErrors() {
    return Flux.range(1, 3)
        .doOnSubscribe(s -> System.out.println("Subscribed"))
        .doOnComplete(() -> System.out.println("--Completed"))
        .map(i -> i / 0)
        .doOnError(err -> System.out.println("--error"));
  }

  public static Mono<String> getWelcomeMessage() {
    return Mono.deferContextual(ctx -> {
      if (ctx.hasKey("user")) {
        return Mono.just("Welcome  " + ctx.get("user"));
      } else {
        return Mono.error(new RuntimeException("unauthenticated"));
      }
    });
  }

}
