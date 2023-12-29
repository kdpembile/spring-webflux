package com.kentisthebest;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

class FluxAndMonoGeneratorServiceTest {

  FluxAndMonoGeneratorService fluxAndMonoGeneratorService =
      new FluxAndMonoGeneratorService();

  @Test
  void namesFlux() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFlux();

    // then
    StepVerifier.create(names)
        //.expectNext("alex", "ben", "chloe")
        //.expectNextCount(3)
        .expectNext("alex")
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  void namesFluxMap() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxMap(3);

    // then
    StepVerifier.create(names)
        //.expectNext("ALEX", "BEN", "CHLOE")
        //.expectNext("ALEX", "CHLOE")
        .expectNext("4-ALEX", "5-CHLOE")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMap() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxFlatMap(3);

    // then
    StepVerifier.create(names)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFluxMapImmutability() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxMapImmutability();

    // then
    StepVerifier.create(names)
        //.expectNext("ALEX", "BEN", "CHLOE")
        .expectNext("alex", "ben", "chloe")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMapAsync() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(3);

    // then
    StepVerifier.create(names)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatConcatMap() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxFlatConcatMap(3);

    // then
    StepVerifier.create(names)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void monoFlatMap() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.monoFlatMap(3);

    // then
    StepVerifier.create(names)
        .expectNext(List.of("A", "L", "E", "X"))
        .verifyComplete();
  }

  @Test
  void namesFluxTransform() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxTransform(3);

    // then
    StepVerifier.create(names)
        .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void concatWithFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.concatWithFlux();

    // then
    StepVerifier.create(s)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();

  }

  @Test
  void concatWithMono() {
    // given
    // when

    var s = fluxAndMonoGeneratorService.concatWithMono();

    // then
    StepVerifier.create(s)
        .expectNext("A", "B")
        .verifyComplete();
  }

  @Test
  void namesFluxTransformDefaultIfEmpty() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxTransformDefaultIfEmpty(6);

    // then
    StepVerifier.create(names)
        .expectNext("default")
        .verifyComplete();
  }

  @Test
  void namesFluxTransformSwitchIfEmpty() {
    // given
    // when
    var names = fluxAndMonoGeneratorService.namesFluxTransformSwitchIfEmpty(6);

    // then
    StepVerifier.create(names)
        .expectNext("D", "E", "F", "A", "U", "L", "T")
        .verifyComplete();
  }

  @Test
  void mergeFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.mergeFlux();

    // then
    StepVerifier.create(s)
        .expectNext("A", "D", "B", "E", "C", "F")
        .verifyComplete();
  }

  @Test
  void mergeWithFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.mergeWithFlux();

    // then
    StepVerifier.create(s)
        .expectNext("A", "D", "B", "E", "C", "F")
        .verifyComplete();
  }

  @Test
  void mergeSequentialFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.mergeSequentialFlux();

    // then
    StepVerifier.create(s)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  void zipWithFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.zipWithFlux();

    // then
    StepVerifier.create(s)
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
  }

  @Test
  void zipFlux() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.zipFlux();

    // then
    StepVerifier.create(s)
        .expectNext("AD14", "BE25", "CF36")
        .verifyComplete();
  }

  @Test
  void zipWithMono() {
    // given
    // when
    var s = fluxAndMonoGeneratorService.zipWithMono();

    // then
    StepVerifier.create(s)
        .expectNext("AB")
        .verifyComplete();
  }
}