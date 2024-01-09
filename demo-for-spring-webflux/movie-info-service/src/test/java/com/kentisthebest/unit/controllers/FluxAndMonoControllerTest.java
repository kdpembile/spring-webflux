package com.kentisthebest.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kentisthebest.controllers.FluxAndMonoController;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void flux() {
    // given
    // when
    // then
    webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .hasSize(3);
  }

  @Test
  void flux_2() {
    // given
    // when
    var flux = webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Integer.class)
        .getResponseBody();

    // then
    StepVerifier.create(flux)
        .expectNext(1, 2, 3)
        .verifyComplete();
  }

  @Test
  void flux_3() {
    // given
    // when
    // then
    webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .consumeWith(listEntityExchangeResult -> {
          var responseBody = listEntityExchangeResult.getResponseBody();

          assert Objects.requireNonNull(responseBody).size() == 3;
        });
  }

  @Test
  void mono() {
    // given
    // when
    // then
    webTestClient.get()
        .uri("/hello-world")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(String.class)
        .consumeWith(StringEntityExchangeResult -> {
          var responseBody = StringEntityExchangeResult.getResponseBody();

          assertEquals("Hello world", responseBody);
        });
  }

  @Test
  void stream() {
    // given
    // when
    var flux = webTestClient.get()
        .uri("/stream")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Long.class)
        .getResponseBody();

    // then
    StepVerifier.create(flux)
        .expectNext(0L, 1L, 2L)
        .thenCancel()
        .verify();
  }

}