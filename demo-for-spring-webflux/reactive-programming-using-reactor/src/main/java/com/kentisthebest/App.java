package com.kentisthebest;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    fluxAndMonoGeneratorService.namesFlux()
        .subscribe(name -> System.out.println("Flux name is: " + name));

    fluxAndMonoGeneratorService.namesFluxMap(3)
        .subscribe(name -> System.out.println("Flux name upper case is: " + name));

    fluxAndMonoGeneratorService.namesFluxFlatMap(3)
        .subscribe(name -> System.out.println("Flux name split: " + name));

    fluxAndMonoGeneratorService.namesFluxMapImmutability()
        .subscribe(name -> System.out.println("Flux name immutability is: " + name));

    fluxAndMonoGeneratorService.nameMono()
        .subscribe(name -> System.out.println("Mono name is: " + name));
  }
}
