package com.kentisthebest.intg.routers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kentisthebest.domains.Review;
import com.kentisthebest.repositories.MovieReviewRepository;
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
class ReviewRouterTest {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  MovieReviewRepository movieReviewRepository;

  @BeforeEach
  void setUp() {
    var reviews = List.of(Review.builder()
            .reviewId(null)
            .movieInfoId(1L)
            .comment("Average movie")
            .rating(7.0)
            .build(),
        Review.builder()
            .reviewId(null)
            .movieInfoId(1L)
            .comment("Awesome movie")
            .rating(8.5)
            .build(),
        Review.builder()
            .reviewId("1")
            .movieInfoId(2L)
            .comment("Excellent Movie")
            .rating(9.0)
            .build());

    movieReviewRepository.saveAll(reviews).blockLast();
  }

  @AfterEach
  void tearDown() {
    movieReviewRepository.deleteAll().block();
  }

  @Test
  void getReviews() {
    // given
    // when
    // then
    webTestClient.get()
        .uri("/v1/reviews")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Review.class)
        .hasSize(3);
  }

  @Test
  void getReviewsByMovieInfoId() {
    // given
    // when
    // then
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/v1/reviews/")
            .queryParam("movieInfoId", "1")
            .build())
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Review.class)
        .hasSize(2);
  }

  @Test
  void addReview() {
    // given
    var review = Review.builder()
        .reviewId(null)
        .movieInfoId(1L)
        .comment("Blockbuster Movie")
        .rating(10.0)
        .build();

    // when
    // then
    webTestClient.post()
        .uri("/v1/reviews")
        .bodyValue(review)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Review.class)
        .consumeWith(reviewEntityExchangeResult -> {

          var savedReview = reviewEntityExchangeResult.getResponseBody();

          assert savedReview != null;
          assert savedReview.getReviewId() != null;
        });
  }

  @Test
  void updateReview() {
    // given
    var review = Review.builder()
        .reviewId("1")
        .movieInfoId(2L)
        .comment("Excellent movie update")
        .rating(8.5)
        .build();

    // when
    // then
    webTestClient.put()
        .uri("/v1/reviews/{id}", "1")
        .bodyValue(review)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(Review.class)
        .consumeWith(reviewEntityExchangeResult -> {

          var updatedReview = reviewEntityExchangeResult.getResponseBody();

          assert updatedReview != null;
          assert updatedReview.getReviewId() != null;

          assertEquals("1", updatedReview.getReviewId());
          assertEquals(2L, updatedReview.getMovieInfoId());
          assertEquals("Excellent movie update", updatedReview.getComment());
          assertEquals(8.5, updatedReview.getRating());
        });
  }

  @Test
  void deleteReview() {
    // given
    // when
    // then
    webTestClient.delete()
        .uri("/v1/reviews/{id}", "1")
        .exchange()
        .expectStatus()
        .isNoContent();
  }

}