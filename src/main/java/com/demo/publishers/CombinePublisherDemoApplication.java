package com.demo.publishers;

import com.demo.util.Utils;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

public class CombinePublisherDemoApplication {

  public static void main(String[] args) {

    System.out.println("********** startWith Demo **********");
    Utils.generateNames().take(2).subscribe(Utils.subscriber("anu "));
    Utils.generateNames().take(3).subscribe(Utils.subscriber("sonu "));

    System.out.println("********** concat Demo **********"); // Lazy subscription and first terminated and second updated
    Flux<Integer> integerFlux1 = Flux.just(1, 2, 3, 4);
    Flux<Integer> integerFlux2 = Flux.just(5, 6, 7, 8);
    Flux<Integer> errorFlux = Flux.error(new RuntimeException("OOPS"));

    Flux<Integer> finalIntegerFlux = integerFlux1.concatWith(integerFlux2);
    finalIntegerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    finalIntegerFlux = Flux.concat(integerFlux1, integerFlux2, errorFlux);
    finalIntegerFlux.subscribe(Utils.onNext(), Utils.onError(), Utils.onComplete());

    System.out.println("********** merge Demo **********"); // Eager subscription and both live
    Flux.merge(Flux.just("OPTIMUS"), Flux.just("PRIME"), Flux.just("GRIMLOCK")).subscribe(Utils.subscriber());

    System.out.println("********** mergeSequential Demo **********");
    Flux.mergeSequential(Flux.just("OPTIMUS"), Flux.just("PRIME")).subscribe(Utils.subscriber());

    System.out.println("********** zip Demo **********");
    Flux.zip(Flux.just("OPTIMUS"), Flux.just("PRIME"), (first, second) -> first + " is " + second).subscribe(Utils.subscriber());

    System.out.println("********** combineLatest Demo **********");
    Flux.combineLatest(Flux.just("OPTIMUS"), Flux.just("PRIME"), (s, i) -> s + i)
        .subscribe(Utils.subscriber());

    System.out.println("********** context Demo **********");
    Utils.getWelcomeMessage()
        .contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase()))
        .contextWrite(Context.of("user", "sam"))
        .subscribe(Utils.subscriber());

  }
}
