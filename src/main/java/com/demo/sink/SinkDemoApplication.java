package com.demo.sink;

import com.demo.util.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class SinkDemoApplication {

  public static void main(String[] args) {

    System.out.println("********** Sink Demo **********");
    Sinks.One<Object> sink = Sinks.one();
    Mono<Object> objectMono = sink.asMono();
    objectMono.subscribe(Utils.subscriber("A"));
    sink.tryEmitValue("value 1");

    System.out.println("********** error demo ********");
    sink = Sinks.one();
    objectMono = sink.asMono();
    objectMono.subscribe(Utils.subscriber());
    sink.tryEmitError(new RuntimeException("error"));

    System.out.println("********** multiple subs demo ********");
    sink = Sinks.one();
    objectMono = sink.asMono();
    objectMono.subscribe(Utils.subscriber("A "));
    objectMono.subscribe(Utils.subscriber("B "));
    sink.tryEmitValue("value 2");

    System.out.println("********** unicast demo ********");
    unicast();

    System.out.println("********** multicast demo ********");
    multicast();

    System.out.println("********** replay demo ********");
    replay();

  }

  private static void unicast() {
    Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();
    Flux<Object> flux = sink.asFlux();
    flux.subscribe(Utils.subscriber("sam"));
    sink.tryEmitNext("hi");
    sink.tryEmitNext("how are you");
    sink.tryEmitNext("?");
  }

  private static void multicast() {
    Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
    Flux<Object> flux = sink.asFlux();
    flux.subscribe(Utils.subscriber("sam"));
    flux.subscribe(Utils.subscriber("john"));
    sink.tryEmitNext("hi");
    sink.tryEmitNext("how are you");
    sink.tryEmitNext("?");
  }

  private static void replay() {
    Sinks.Many<Object> sink = Sinks.many().replay().all();

    Flux<Object> flux = sink.asFlux();
    sink.tryEmitNext("hi");
    sink.tryEmitNext("how are you");

    flux.subscribe(Utils.subscriber("sam"));
    flux.subscribe(Utils.subscriber("mike"));
    sink.tryEmitNext("?");
    flux.subscribe(Utils.subscriber("jake"));

    sink.tryEmitNext("new msg");
  }


}
