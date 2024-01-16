package com.kentisthebest.unit.routers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import com.kentisthebest.domains.Review;
import com.kentisthebest.handlers.ReviewHandler;
import com.kentisthebest.repositories.MovieReviewRepository;
import com.kentisthebest.routers.ReviewRouter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
@AutoConfigureWebTestClient
class ReviewRouterTest {

  @MockBean
  MovieReviewRepository movieReviewRepository;

  @Autowired
  WebTestClient webTestClient;

  @Test
  void getReviews() {
    // given
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

    // when
    when(movieReviewRepository.findAll())
        .thenReturn(Flux.fromIterable(reviews));

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
            .build());

    // when
    when(movieReviewRepository.findReviewsByMovieInfoId(isA(Long.class)))
        .thenReturn(Flux.fromIterable(reviews));

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
    when(movieReviewRepository.save(isA(Review.class)))
        .thenReturn(Mono.just(Review.builder()
            .reviewId("2")
            .movieInfoId(1L)
            .comment("Blockbuster Movie")
            .rating(10.0)
            .build()));

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

          assertEquals("2", savedReview.getReviewId());
        });
  }

  @Test
  void addReview_whenMovieInfoIdIsNotNullAndRatingIsNegative_thenReturnBadRequest() {
    // given
    var review = Review.builder()
        .reviewId(null)
        .movieInfoId(null)
        .comment("Blockbuster Movie")
        .rating(-9.0)
        .build();

    // when
    when(movieReviewRepository.save(isA(Review.class)))
        .thenReturn(Mono.just(Review.builder()
            .reviewId("2")
            .movieInfoId(1L)
            .comment("Blockbuster Movie")
            .rating(10.0)
            .build()));

    // then
    webTestClient.post()
        .uri("/v1/reviews")
        .bodyValue(review)
        .exchange()
        .expectStatus()
        .is5xxServerError();
  }

  @Test
  void updateReview() {
    // given
    var existingMovieReview = Review.builder()
        .reviewId("1")
        .movieInfoId(2L)
        .comment("Excellent Movie")
        .rating(9.0)
        .build();

    var review = Review.builder()
        .reviewId("1")
        .movieInfoId(2L)
        .comment("Excellent movie update")
        .rating(8.5)
        .build();

    // when
    when(movieReviewRepository.findById(isA(String.class)))
        .thenReturn(Mono.just(existingMovieReview));
    when(movieReviewRepository.save(isA(Review.class)))
        .thenReturn(Mono.just(review));

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
    var reviewId = "1";

    var review = Review.builder()
        .reviewId("1")
        .movieInfoId(2L)
        .comment("Excellent Movie")
        .rating(9.0)
        .build();

    // when
    when(movieReviewRepository.findById(isA(String.class)))
        .thenReturn(Mono.just(review));
    when(movieReviewRepository.deleteById(reviewId))
        .thenReturn(Mono.empty().then());

    // then
    webTestClient.delete()
        .uri("/v1/reviews/{id}", reviewId)
        .exchange()
        .expectStatus()
        .isNoContent();
  }
}
