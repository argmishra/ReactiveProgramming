package com.demo.operator;

import com.demo.util.Utils;
import java.time.Duration;
import reactor.core.publisher.Flux;

public class OperatorDemoApplication {

  public static void main(String[] args) throws InterruptedException {

    System.out.println("********** handle Demo **********");
    Flux.range(1, 20).handle((integer, synchronousSink) -> {
      if (integer % 2 == 0) {
        synchronousSink.next("EVEN : " + integer);
      } else {
        synchronousSink.next("ODD : " + integer);
      }
    }).subscribe(Utils.subscriber());

    System.out.println("********** hooks Demo **********");
    Flux.create(fluxSink -> {
          System.out.println("inside create");
          for (int i = 0; i < 5; i++) {
            fluxSink.next(i);
          }
          fluxSink.complete();
          //fluxSink.error(new RuntimeException("oops"));
          System.out.println("--completed");
        })
        .doFirst(() -> System.out.println("doFirst"))
        .doOnSubscribe(s -> System.out.println("doOnSubscribe : " + s))
        .doOnRequest(l -> System.out.println("doOnRequest : " + l))
        .doOnNext(o -> System.out.println("doOnNext : " + o))
        .doOnCancel(() -> System.out.println("doOnCancel"))
        .doOnDiscard(Object.class, o -> System.out.println("doOnDiscard : " + o))
        .doOnError(err -> System.out.println("doOnError : " + err.getMessage()))
        .doOnComplete(() -> System.out.println("doOnComplete"))
        .doOnTerminate(() -> System.out.println("doOnTerminate"))
        .doFinally(signal -> System.out.println("doFinally 1 : " + signal))
        .subscribe(Utils.subscriber());

    System.out.println("********** defaultIfEmpty Demo **********");
    Flux.range(1, 10).filter(i -> i > 15).defaultIfEmpty(1000).subscribe(Utils.subscriber());

    System.out.println("********** switchIfEmpty Demo **********");
    Flux.range(1, 10).filter(i -> i > 15).switchIfEmpty(Flux.range(100, 2))
        .subscribe(Utils.subscriber());

    System.out.println("********** onError Demo **********");
    Flux.range(1, 10)
        .map(i -> 10 / (5 - i))
        .onErrorReturn(-1)
        //.onErrorResume(e -> Utils.fallback())
        .subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** limitRate Demo **********");
    Flux.range(1, 5).log().limitRate(2).subscribe(Utils.subscriber());

    System.out.println("********** delay Demo **********");
    Flux.range(1, 5).delayElements(Duration.ofSeconds(1)).subscribe(Utils.subscriber());
    Thread.sleep(1000);

    System.out.println("********** transform Demo **********");
    Flux.range(1, 5).transform(Utils.userFilter()).subscribe(Utils.subscriber());

    System.out.println("********** repeat Demo **********");
    Utils.getIntegers().repeat(1).subscribe(Utils.subscriber());

    System.out.println("********** retry Demo **********");
    Utils.getIntegersWithErrors().retry(1).subscribe(Utils.subscriber());

  }

}