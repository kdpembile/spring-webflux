package com.kentisthebest.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kentisthebest.controllers.MovieInfoController;
import com.kentisthebest.models.MovieInfo;
import com.kentisthebest.services.MovieInfoService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private MovieInfoService movieInfoService;

  @Test
  void addMovieInfo() {
    // given
    var movieInfo = MovieInfo.builder()
        .id(null)
        .name("Back to the future")
        .year(1985)
        .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
        .releaseDate(LocalDate.parse("1985-07-03"))
        .build();

    // when
    when(movieInfoService.addMovieInfo(movieInfo))
        .thenReturn(Mono.just(MovieInfo.builder()
            .id("mockId")
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build()));

    // then
    webTestClient.post()
        .uri("/v1/movie-info")
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {

          var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

          assert savedMovieInfo != null;
          assert savedMovieInfo.getId() != null;
          assertEquals("mockId", savedMovieInfo.getId());
        });
  }

  @Test
  void addMovieInfo_whenNameIsBlank_thenReturnBadRequest() {
    // given
    var movieInfo = MovieInfo.builder()
        .id(null)
        .name("")
        .year(1985)
        .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
        .releaseDate(LocalDate.parse("1985-07-03"))
        .build();

    // when
    when(movieInfoService.addMovieInfo(movieInfo))
        .thenReturn(Mono.just(MovieInfo.builder()
            .id("mockId")
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build()));

    // then
    webTestClient.post()
        .uri("/v1/movie-info")
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .consumeWith(stringEntityExchangeResult -> {
          var responseBody = stringEntityExchangeResult.getResponseBody();

          System.out.println("responseBody: " + responseBody);

          assert responseBody != null;
          assertEquals("movieInfo.name must be present", responseBody);
        });
  }

  @Test
  void addMovieInfo_whenCastIsBlank_thenReturnBadRequest() {
    // given
    var movieInfo = MovieInfo.builder()
        .id(null)
        .name("Back to the future")
        .year(1985)
        .cast(List.of(""))
        .releaseDate(LocalDate.parse("1985-07-03"))
        .build();

    // when
    when(movieInfoService.addMovieInfo(movieInfo))
        .thenReturn(Mono.just(MovieInfo.builder()
            .id("mockId")
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build()));

    // then
    webTestClient.post()
        .uri("/v1/movie-info")
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .consumeWith(stringEntityExchangeResult -> {
          var responseBody = stringEntityExchangeResult.getResponseBody();

          System.out.println("responseBody: " + responseBody);

          assert responseBody != null;
          assertEquals("movieInfo.cast must be present", responseBody);
        });
  }

  @Test
  void getAllMovieInfo() {
    // given
    var movieInfo = List.of(new MovieInfo(null, "Batman Begins",
            2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight",
            2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Dark Knight Rises",
            2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

    // when
    when(movieInfoService.getAllMovieInfo())
        .thenReturn(Flux.fromIterable(movieInfo));

    // then
    webTestClient.get()
        .uri("/v1/movie-info")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(MovieInfo.class)
        .hasSize(3);
  }

  @Test
  void getMovieInfoById() {
    // given
    var id = "abc";
    var movieInfo = new MovieInfo(id, "Dark Knight Rises",
        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

    // when
    when(movieInfoService.getMovieInfoById(id))
        .thenReturn(Mono.just(movieInfo));

    // then
    webTestClient.get()
        .uri("/v1/movie-info/{id}", "abc")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Dark Knight Rises");
  }

  @Test
  void updateMovieInfo() {
    // given
    var id = "abc";
    var movieInfo = new MovieInfo(id, "Dark Knight Rises",
        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

    // when
    when(movieInfoService.updateMovieInfo(movieInfo, id))
        .thenReturn(Mono.just(MovieInfo.builder()
            .id("123")
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build()));

    // then
    webTestClient.put()
        .uri("/v1/movie-info/{id}", id)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {

          var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

          assert updatedMovieInfo != null;
          assert updatedMovieInfo.getId() != null;

          assertEquals("123", updatedMovieInfo.getId());
          assertEquals("Back to the future", updatedMovieInfo.getName());
        });

  }

  @Test
  void deleteMovieInfo() {
    // given
    var id = "abc";

    // when
    when(movieInfoService.deleteMovieInfo(id))
        .thenReturn(Mono.empty().then());

    // then
    webTestClient.delete()
        .uri("/v1/movie-info/{id}", id)
        .exchange()
        .expectStatus()
        .isNoContent();
  }
}