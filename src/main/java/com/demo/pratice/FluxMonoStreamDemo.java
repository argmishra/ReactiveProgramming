package com.demo.pratice;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

public class FluxMonoStreamDemo {

  public static void main(String[] args) throws InterruptedException {
    FluxMonoStreamDemo fluxMonoDemoApplication = new FluxMonoStreamDemo();

    fluxMonoDemoApplication.namesFlux()
        .subscribe(name -> System.out.println("Flux Demo : " + name));

    fluxMonoDemoApplication.nameMono().subscribe(name -> System.out.println("Mono Demo : " + name));

    // One to One Transform
    fluxMonoDemoApplication.namesFlux_map()
        .subscribe(name -> System.out.println("Map Demo : " + name));

    // Check condition
    fluxMonoDemoApplication.namesFlux_map_filter(3)
        .subscribe(name -> System.out.println("Filter Demo : " + name));

    // One to N Transform and no ordering
    fluxMonoDemoApplication.namesFlux_flatMap_filter(3)
        .subscribe(name -> System.out.println("FlatMap Flux Demo : " + name));

    // Delay Elements
    fluxMonoDemoApplication.namesFlux_flatMap_filter_delayElements(3)
        .subscribe(name -> System.out.println("DelayElements Demo : " + name));

    // One to N Transform and ordered
    fluxMonoDemoApplication.namesFlux_contactMap_filter(3)
        .subscribe(name -> System.out.println("ConcatMap Demo : " + name));

    // One to N Transform and ordered
    fluxMonoDemoApplication.nameMono_map_flatMap(3)
        .subscribe(name -> System.out.println("flatMap Mono Demo : " + name));

    // When Mono return Flux
    fluxMonoDemoApplication.nameMono_map_flatMapMany(3)
        .subscribe(name -> System.out.println("flatMapMany Mono Demo : " + name));

    // Common Function
    fluxMonoDemoApplication.namesFlux_transform(3)
        .subscribe(name -> System.out.println("Transform Flux Demo : " + name));

    // Default Value
    fluxMonoDemoApplication.namesFlux_defaultIfEmpty(30)
        .subscribe(name -> System.out.println("DefaultIfEmpty Flux Demo : " + name));

    // Switch to given
    fluxMonoDemoApplication.namesFlux_switchIfEmpty(30)
        .subscribe(name -> System.out.println("SwitchIfEmpty Flux Demo : " + name));

    // lazy concat using static method in sequence
    fluxMonoDemoApplication.concat_flux()
        .subscribe(name -> System.out.println("concat Flux Demo : " + name));

    // lazy concat using instance method in sequence
    fluxMonoDemoApplication.concatWith_flux()
        .subscribe(name -> System.out.println("concatWith Flux Demo : " + name));

    // eager merge with sequence using instance method
    fluxMonoDemoApplication.mergeSequential_flux()
        .subscribe(name -> System.out.println("mergeSequential Flux Demo : " + name));

    // Combine stream till all emits
    fluxMonoDemoApplication.zip_flux()
        .subscribe(name -> System.out.println("zip Flux Demo : " + name));

    // Combine stream till all emits
    fluxMonoDemoApplication.zipWith_flux()
        .subscribe(name -> System.out.println("zipWith Flux Demo : " + name));

    // parallel
    fluxMonoDemoApplication.parallel()
        .subscribe(name -> System.out.println("parallel Flux Demo : " + name));

    // eager merge using static method
    fluxMonoDemoApplication.merge_flux()
        .subscribe(name -> System.out.println("merge Flux Demo : " + name));

    Thread.sleep(1000);

    // eager merge using instance method
    fluxMonoDemoApplication.mergeWith_flux()
        .subscribe(name -> System.out.println("mergeWith Flux Demo : " + name));

    Thread.sleep(1000);

  }

  public Flux<String> parallel() {
    return Flux.fromIterable(List.of("A", "B", "C")).map(s -> s.toUpperCase());
  }

  public ParallelFlux<String> parallel_demo() {
    return Flux.fromIterable(List.of("A", "B", "C")).parallel().runOn(Schedulers.parallel())
        .map(s -> s.toUpperCase());
  }

  public Flux<String> zipWith_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    return abcFlux.zipWith(defFlux).map((a) -> a.getT1() + a.getT2());
  }

  public Flux<String> zip_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    var ghiFlux = Flux.just("G", "H", "I");

    return Flux.zip(abcFlux, defFlux, ghiFlux).map((a) -> a.getT1() + a.getT2() + a.getT3());
  }

  public Flux<String> mergeSequential_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    return Flux.mergeSequential(abcFlux, defFlux);
  }

  public Flux<String> merge_flux() {
    var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
    var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(60));
    return Flux.merge(abcFlux, defFlux);
  }

  public Flux<String> mergeWith_flux() {
    var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
    var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(60));
    return abcFlux.mergeWith(defFlux);
  }

  public Flux<String> concat_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    return Flux.concat(abcFlux, defFlux);
  }

  public Flux<String> concatWith_flux() {
    var abcFlux = Flux.just("A", "B", "C");
    var defFlux = Flux.just("D", "E", "F");
    return abcFlux.concatWith(defFlux);
  }

  public Flux<String> namesFlux() {
    return Flux.fromIterable(List.of("alex", "ben", "chloe"));
  }

  public Mono<String> nameMono() {
    return Mono.just("alex");
  }

  public Mono<List<String>> nameMono_map_flatMap(int length) {
    return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > length)
        .flatMap(s -> splitStringMono(s));
  }

  public Flux<String> nameMono_map_flatMapMany(int length) {
    return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > length)
        .flatMapMany(s -> splitString(s)).log();
  }

  public Flux<String> namesFlux_map() {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase);
  }

  public Flux<String> namesFlux_immutable() {
    Flux<String> stringFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
    stringFlux.map(String::toUpperCase);
    return stringFlux;
  }

  public Flux<String> namesFlux_map_filter(int length) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase)
        .filter(s -> s.length() > length).map(s -> s.length() + "-" + s);
  }

  public Flux<String> namesFlux_flatMap_filter(int length) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase)
        .filter(s -> s.length() > length).flatMap(s -> splitString(s));
  }

  private Flux<String> splitString(String name) {
    return Flux.fromArray(name.split(""));
  }

  public Flux<String> namesFlux_flatMap_filter_delayElements(int length) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase)
        .filter(s -> s.length() > length).flatMap(s -> splitString_delay(s));
  }

  private Flux<String> splitString_delay(String name) {
    return Flux.fromArray(name.split("")).delayElements(Duration.ofMillis(1000));
  }

  public Flux<String> namesFlux_contactMap_filter(int length) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase)
        .filter(s -> s.length() > length).concatMap(s -> splitString_delay(s));
  }

  private Mono<List<String>> splitStringMono(String name) {
    return Mono.just(List.of(name.split("")));
  }

  public Flux<String> namesFlux_transform(int length) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > length);
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap)
        .flatMap(s -> splitString(s));
  }

  public Flux<String> namesFlux_defaultIfEmpty(int length) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > length);
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap)
        .flatMap(s -> splitString(s)).defaultIfEmpty("default");
  }

  public Flux<String> namesFlux_switchIfEmpty(int length) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > length);
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap)
        .flatMap(s -> splitString(s)).switchIfEmpty(Mono.just("Switch"));
  }


}
