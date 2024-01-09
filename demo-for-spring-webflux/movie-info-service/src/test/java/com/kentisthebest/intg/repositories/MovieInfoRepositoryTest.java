package com.kentisthebest.intg.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kentisthebest.models.MovieInfo;
import com.kentisthebest.repositories.MovieInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
class MovieInfoRepositoryTest {

  @Autowired
  MovieInfoRepository movieInfoRepository;

  @BeforeEach
  void setUp() {
    var movieInfo = List.of(new MovieInfo(null, "Batman Begins",
            2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight",
            2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Dark Knight Rises",
            2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

    // blocking for saving was used because when we call findall(), we might not fetch any data
    // blocking calls are only allowed in test cases
    movieInfoRepository.saveAll(movieInfo)
        .blockLast();
  }

  @AfterEach
  void tearDown() {
    movieInfoRepository.deleteAll().block();
  }

  @Test
  void findAll() {
    // given
    // when
    var movieInfoFlux = movieInfoRepository.findAll().log();

    // then
    StepVerifier.create(movieInfoFlux)
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void findById() {
    // given
    // when
    var movieInfoMono = movieInfoRepository.findById("abc").log();

    // then
    StepVerifier.create(movieInfoMono)
        .assertNext(movieInfo -> {
          assertEquals("Dark Knight Rises", movieInfo.getName());
          assertEquals(2012, movieInfo.getYear());
          assertEquals(List.of("Christian Bale", "Tom Hardy"), movieInfo.getCast());
          assertEquals(LocalDate.parse("2012-07-20"), movieInfo.getReleaseDate());

        })
        .verifyComplete();
  }

  @Test
  void saveMovieInfo() {
    // given
    var backToTheFutureInfo = MovieInfo.builder()
        .id(null)
        .name("Back to the future")
        .year(1985)
        .cast(List.of("Michael J. Fox", "Christopher Lloyd"))
        .releaseDate(LocalDate.parse("1985-07-03"))
        .build();

    // when
    var movieInfoMono = movieInfoRepository.save(backToTheFutureInfo).log();

    // then
    StepVerifier.create(movieInfoMono)
        .assertNext(movieInfo -> {
          assertNotNull(movieInfo.getName());
          assertEquals(1985, movieInfo.getYear());
          assertEquals(List.of("Michael J. Fox", "Christopher Lloyd"), movieInfo.getCast());
          assertEquals(LocalDate.parse("1985-07-03"), movieInfo.getReleaseDate());

        })
        .verifyComplete();
  }

  @Test
  void updateMovieInfo() {
    // given
    // when
    var darkNightRisesInfo = movieInfoRepository.findById("abc").block();
    Objects.requireNonNull(darkNightRisesInfo).setYear(2021);

    // then
    StepVerifier.create(Mono.just(darkNightRisesInfo))
        .assertNext(movieInfo -> {
          assertEquals(2021, movieInfo.getYear());

        })
        .verifyComplete();
  }

  @Test
  void deleteMovieInfo() {
    // given
    // when
    movieInfoRepository.deleteById("abc").block();
    var movieInfo = movieInfoRepository.findAll().log();

    // then
    StepVerifier.create(movieInfo)
        .expectNextCount(2)
        .verifyComplete();
  }

}