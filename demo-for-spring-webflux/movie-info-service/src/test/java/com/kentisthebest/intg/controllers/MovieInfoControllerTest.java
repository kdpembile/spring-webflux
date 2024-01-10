package com.kentisthebest.intg.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kentisthebest.models.MovieInfo;
import com.kentisthebest.repositories.MovieInfoRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

  @Autowired
  MovieInfoRepository movieInfoRepository;

  @Autowired
  WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    var movieInfo = List.of(new MovieInfo(null, "Batman Begins",
            2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight",
            2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Dark Knight Rises",
            2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

    // blocking for saving was used because when we call findAll(), we might not fetch any data
    // blocking calls are only allowed in test cases
    movieInfoRepository.saveAll(movieInfo)
        .blockLast();
  }

  @AfterEach
  void tearDown() {
    movieInfoRepository.deleteAll().block();
  }

  @Test
  void addMovieInfo() {
    // given
    // when
    webTestClient.post()
        .uri("/v1/movie-info")
        .bodyValue(MovieInfo.builder()
            .id(null)
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build())
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {

          var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

          assert savedMovieInfo != null;
          assert savedMovieInfo.getId() != null;
        });

    // then

  }

  @Test
  void getAllMovieInfo() {
    // given
    // when
    webTestClient.get()
        .uri("/v1/movie-info")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(MovieInfo.class)
        .hasSize(3);

    // then
  }

  @Test
  void getMovieInfoById() {
    // given
    // when
    webTestClient.get()
        .uri("/v1/movie-info/{id}", "abc")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Dark Knight Rises");
//        .expectBody(MovieInfo.class)
//        .consumeWith(movieInfoEntityExchangeResult -> {
//
//          var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
//          assertNotNull(movieInfo);
//          assertEquals("abc", movieInfo.getId());
//          assertEquals("Dark Knight Rises", movieInfo.getName());
//          assertEquals(2012, movieInfo.getYear());
//          assertEquals(List.of("Christian Bale", "Tom Hardy"), movieInfo.getCast());
//          assertEquals(LocalDate.parse("2012-07-20"), movieInfo.getReleaseDate());
//        });

    // then
  }

  @Test
  void getMovieInfoById_notFound() {
    // given
    // when
    webTestClient.get()
        .uri("/v1/movie-info/{id}", "def")
        .exchange()
        .expectStatus()
        .isNotFound();

    // then
  }

  @Test
  void updateMovieInfo() {
    // given
    // when
    webTestClient.put()
        .uri("/v1/movie-info/{id}", "abc")
        .bodyValue(MovieInfo.builder()
            .id(null)
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build())
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {

          var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

          assert updatedMovieInfo != null;
          assert updatedMovieInfo.getId() != null;

          assertEquals("Back to the future", updatedMovieInfo.getName());
        });

    // then
  }

  @Test
  void updateMovieInfo_notFound() {
    // given
    // when
    webTestClient.put()
        .uri("/v1/movie-info/{id}", "def")
        .bodyValue(MovieInfo.builder()
            .id(null)
            .name("Back to the future")
            .year(1985)
            .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
            .releaseDate(LocalDate.parse("1985-07-03"))
            .build())
        .exchange()
        .expectStatus()
        .isNotFound();

    // then
  }

  @Test
  void deleteMovieInfo() {
    // given
    // when
    webTestClient.delete()
        .uri("/v1/movie-info/{id}", "abc")
        .exchange()
        .expectStatus()
        .isNoContent();

    // then
  }
}