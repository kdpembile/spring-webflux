package com.kentisthebest.controllers;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxAndMonoController {

  @GetMapping(path = "/flux")
  public Flux<Integer> flux() {
    return Flux.just(1, 2, 3).log();
  }

  @GetMapping(path = "/hello-world")
  public Mono<String> helloWorldMono() {
    return Mono.just("Hello world").log();
  }

  @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Long> stream() {
    return Flux.interval(Duration.ofSeconds(1)).log();
  }

}
