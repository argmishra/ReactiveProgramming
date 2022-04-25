package com.demo.pratice;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ExceptionHandlingStream {

  public static void main(String[] args) {
    ExceptionHandlingStream exceptionHandlingStream = new ExceptionHandlingStream();

    // Exception or Error Demo
    exceptionHandlingStream.exception_flux()
        .subscribe(name -> System.out.println("Error Demo : " + name));

    // onErrorReturn  - return default value and continue
    exceptionHandlingStream.onErrorReturn_flux()
        .subscribe(name -> System.out.println("onErrorReturn Demo : " + name));

    System.out.println("******************************************************************");

    // onErrorResume  - return function and continue
    exceptionHandlingStream.onErrorResume_flux(new RuntimeException("Exception"))
        .subscribe(name -> System.out.println("onErrorResume Demo : " + name));

    System.out.println("******************************************************************");

    exceptionHandlingStream.onErrorResume_flux(new InterruptedException("Exception"))
        .subscribe(name -> System.out.println("onErrorResume1 Demo : " + name));

    System.out.println("******************************************************************");

    // onErrorContinue  - leave error emit and continue
    exceptionHandlingStream.onErrorContinue_flux()
        .subscribe(name -> System.out.println("onErrorContinue Demo : " + name));

    System.out.println("******************************************************************");

    // onErrorMap - throw user define exception and discontinue
    exceptionHandlingStream.onErrorMap_flux()
        .subscribe(name -> System.out.println("onErrorMap Demo : " + name));

    System.out.println("******************************************************************");

    // onErrorMap - throw user define exception and discontinue
    exceptionHandlingStream.doOnError_flux()
        .subscribe(name -> System.out.println("doOnError Demo : " + name));

  }

  public Flux<String> doOnError_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    return abcFlux.concatWith(Flux.error(new Exception("Exception")))
        .doOnError(throwable -> {
          System.out.println(throwable.getMessage());
        })
        .concatWith(Flux.just("D", "E", "F"));
  }

  public Flux<String> onErrorMap_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    return abcFlux.map(s -> {
      if (s.equals("A")) {
        throw new IllegalStateException("Exception");
      }
      return s;
    }).onErrorMap(ex -> {
      return new ReactorException(ex, ex.getMessage());
    }).concatWith(Flux.just("D", "E", "F"));
  }

  public Flux<String> onErrorContinue_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    return abcFlux.map(s -> {
      if (s.equals("A")) {
        throw new IllegalStateException("Exception");
      }
      return s;
    }).onErrorContinue((ex, name) -> {
      log.error("Error message  : {}  for name : {} ", ex, name);
    }).concatWith(Flux.just("D", "E", "F"));
  }

  public Flux<String> onErrorResume_flux(Exception e) {
    var abcFlux = Flux.just("A", "B", "C");
    var recoveryFlux = Flux.just("R");

    return abcFlux.concatWith(Flux.error(e))
        .onErrorResume(ex -> {
          log.error("Error message  : {} ", ex);
          if (ex instanceof RuntimeException) {
            return recoveryFlux;
          } else {
            return Flux.error(ex);
          }
        }).concatWith(Flux.just("D", "E", "F"));
  }

  public Flux<String> onErrorReturn_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    return abcFlux.concatWith(Flux.error(new Exception("Exception")))
        .onErrorReturn("Z")
        .concatWith(Flux.just("D", "E", "F"));
  }

  public Flux<String> exception_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    return abcFlux.concatWith(Flux.error(new RuntimeException("Exception")))
        .concatWith(Flux.just("D", "E", "F"));
  }

}
