package com.demo.util;

import com.github.javafaker.Faker;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;

public class Utils {

  private static final Faker faker = Faker.instance();

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

}
