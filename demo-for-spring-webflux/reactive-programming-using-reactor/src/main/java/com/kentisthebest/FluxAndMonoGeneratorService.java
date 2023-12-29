package com.kentisthebest;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

  private final List<String> names = List.of("alex", "ben", "chloe");

  public Flux<String> namesFlux() {
    return Flux.fromIterable(names)
        .log();
  }

  public Flux<String> namesFluxMap(int stringLength) {
    return Flux.fromIterable(names)
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .map(s -> s.length() + "-" + s)
        .log();
  }

  public Flux<String> namesFluxFlatMap(int stringLength) {
    return Flux.fromIterable(names)
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitName)
        .log();
  }

  public Flux<String> namesFluxFlatMapAsync(int stringLength) {
    return Flux.fromIterable(names)
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitNameWithDelay)
        .log();
  }

  public Flux<String> namesFluxFlatConcatMap(int stringLength) {
    return Flux.fromIterable(names)
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .concatMap(this::splitNameWithDelay)
        .log();
  }

  public Flux<String> namesFluxTransform(int stringLength) {
    Function<Flux<String>, Flux<String>> filterMap = name ->
        name.map(String::toUpperCase)
            .filter(s -> s.length() > stringLength);

    return Flux.fromIterable(names)
        .transform(filterMap)
        .flatMap(this::splitName)
        .log();
  }

  public Flux<String> namesFluxTransformDefaultIfEmpty(int stringLength) {
    Function<Flux<String>, Flux<String>> filterMap = name ->
        name.map(String::toUpperCase)
            .filter(s -> s.length() > stringLength);

    return Flux.fromIterable(names)
        .transform(filterMap)
        .flatMap(this::splitName)
        .defaultIfEmpty("default")
        .log();
  }

  public Flux<String> namesFluxTransformSwitchIfEmpty(int stringLength) {
    Function<Flux<String>, Flux<String>> filterMap = name ->
        name.map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .flatMap(this::splitName);

    var defaultFlux = Flux.just("default")
        .transform(filterMap);

    return Flux.fromIterable(names)
        .transform(filterMap)
        .switchIfEmpty(defaultFlux)
        .log();
  }

  public Flux<String> concatWithFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");

    Flux<String> defFlux = Flux.just("D", "E", "F");

    return abcFlux.concatWith(defFlux).log();
  }

  public Flux<String> concatWithMono() {

    Mono<String> aMono = Mono.just("A");

    Mono<String> bMono = Mono.just("B");

    return aMono.concatWith(bMono).log();
  }


  public Flux<String> mergeFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    return Flux.merge(abcFlux, defFlux).log();
  }

  public Flux<String> mergeWithFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    return abcFlux.mergeWith(defFlux).log();
  }

  public Flux<String> mergeSequentialFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    return Flux.mergeSequential(abcFlux, defFlux).log();
  }

  public Flux<String> zipWithFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");

    Flux<String> defFlux = Flux.just("D", "E", "F");

    return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
  }

  public Mono<String> zipWithMono() {

    Mono<String> aMono = Mono.just("A");

    Mono<String> bMono = Mono.just("B");

    return aMono.zipWith(bMono, (first, second) -> first + second).log();
  }


  public Flux<String> zipFlux() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");

    Flux<String> defFlux = Flux.just("D", "E", "F");

    Flux<String> _123Flux = Flux.just("1", "2", "3");

    Flux<String> _456Flux = Flux.just("4", "5", "6");

    return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
        .map(t -> t.getT1() + t.getT2() + t.getT3() + t.getT4())
        .log();
  }

  public Flux<String> namesFluxMapImmutability() {
    Flux<String> fluxNames = Flux.fromIterable(names);
    fluxNames.map(String::toUpperCase);

    return fluxNames;
  }

  public Mono<String> nameMono() {
    return Mono.just("alex")
        .log();
  }

  public Mono<List<String>> monoFlatMap(int stringLength) {
    return Mono.just("alex")
        .map(String::toUpperCase)
        .filter(s -> s.length() > stringLength)
        .flatMap(this::splitStringMono)
        .log();
  }

  private Mono<List<String>> splitStringMono(String s) {
    String[] chars = s.split("");
    return Mono.just(List.of(chars));
  }

  public Flux<String> splitName(String name) {
    return Flux.fromArray(name.split(""));
  }

  public Flux<String> splitNameWithDelay(String name) {

    Random random = new Random(1000);

    return Flux.fromArray(name.split(""))
        .delayElements(Duration.ofMillis(random.nextInt(1000)));
  }
}
