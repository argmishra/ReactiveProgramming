package com.demo.flux;

import com.demo.util.Utils;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxDemoApplication {

  public static void main(String[] args) throws InterruptedException {

    System.out.println("********** Empty Demo **********");
    Flux<Integer> integerFlux = Flux.empty();
    integerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** Demo **********");
    integerFlux = Flux.just(1, 2, 3, 4);
    integerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** multiple subscriber Demo **********");
    Flux<Integer> evenFlux = integerFlux.filter(i -> (i % 2) == 0);
    integerFlux.subscribe(i -> System.out.println("Integer Flux : " + i));
    evenFlux.subscribe(i -> System.out.println("Even Integer Flux : " + i));

    System.out.println("********** from List Demo **********");
    List<String> stringList = List.of("a", "b", "c", "d");
    Flux.fromIterable(stringList).subscribe(Utils.onNext());

    System.out.println("********** from Array Demo **********");
    Integer[] integerArray = {1, 2, 3, 4, 5};
    Flux.fromArray(integerArray).subscribe(Utils.onNext());

    System.out.println("********** fromStream Demo **********");
    integerFlux = Flux.fromStream(() -> Arrays.asList(integerArray).stream());
    integerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());
    integerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** Range Demo **********");
    Flux.range(1, 10).map(i -> Utils.faker().name().firstName()).subscribe(Utils.onNext());

    System.out.println("********** log Demo **********");
    Flux.range(1, 2).log().subscribe(Utils.onNext());

    System.out.println("********** from Mono Demo **********");
    Mono<Integer> integerMono = Mono.just(1);
    integerFlux = Flux.from(integerMono);
    integerFlux.subscribe(Utils.onNext());

    System.out.println("********** to Mono Demo **********");
    integerMono = Flux.range(1, 22).next();
    integerMono.subscribe(i -> System.out.println(i));

    System.out.println("********** FluxSink Demo **********");
    Flux.create(fluxSink -> {
      fluxSink.next(1);
      fluxSink.next(2);
      fluxSink.complete();
    }).subscribe(Utils.subscriber());

    System.out.println("********** take Demo **********");
    Flux.range(1, 22).take(5).subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** generate Demo **********");
    Flux.generate(synchronousSink -> {
          synchronousSink.next(Utils.faker().country().name());
          synchronousSink.complete();
        }).take(2)
        .subscribe(Utils.subscriber());

    System.out.println("********** interval Demo **********");
    Flux.interval(Duration.ofSeconds(1)).subscribe(Utils.onNext());
    Thread.sleep(3000);

  }
}