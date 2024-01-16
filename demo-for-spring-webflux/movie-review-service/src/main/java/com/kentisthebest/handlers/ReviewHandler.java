package com.kentisthebest.handlers;

import com.kentisthebest.domains.Review;
import com.kentisthebest.exceptions.ReviewDataException;
import com.kentisthebest.exceptions.ReviewNotFoundException;
import com.kentisthebest.repositories.MovieReviewRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReviewHandler {

  private final Validator validator;

  private final MovieReviewRepository movieReviewRepository;

  public ReviewHandler(MovieReviewRepository movieReviewRepository, Validator validator) {
    this.movieReviewRepository = movieReviewRepository;
    this.validator = validator;
  }

  public Mono<ServerResponse> addReview(ServerRequest request) {
    return request.bodyToMono(Review.class)
        .doOnNext(this::validate)
        .flatMap(movieReviewRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
  }

  private void validate(Review review) {
    var constraintViolations = validator.validate(review);

    log.info("constraintViolations: {}", constraintViolations);

    if (!constraintViolations.isEmpty()) {

      var errorMessage = constraintViolations
          .stream()
          .map(ConstraintViolation::getMessage)
          .sorted()
          .collect(Collectors.joining(","));

      throw new ReviewDataException(errorMessage);
    }
  }

  public Mono<ServerResponse> getReviews(ServerRequest request) {
    var movieInfoId = request.queryParam("movieInfoId");

    if (movieInfoId.isPresent()) {
      var reviews = movieReviewRepository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
      return buildReviewResponse(reviews);
    }

    var reviews = movieReviewRepository.findAll();

    return buildReviewResponse(reviews);
  }

  private Mono<ServerResponse> buildReviewResponse(Flux<Review> reviews) {
    return ServerResponse.ok().body(reviews, Review.class);
  }

  public Mono<ServerResponse> updateReview(ServerRequest request) {
    var reviewId = request.pathVariable("id");
    // var message = "Review not found for the given review id: " + reviewId;

    var existingMovieReview = movieReviewRepository.findById(reviewId);
        // .switchIfEmpty(Mono.error(new ReviewNotFoundException(message)));

    return existingMovieReview
        .flatMap(review ->
            request.bodyToMono(Review.class)
                .map(reqReview -> {
                  review.setComment(reqReview.getComment());
                  review.setRating(reqReview.getRating());

                  return review;
                })
                .flatMap(movieReviewRepository::save)
                .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview))
        ).switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteReview(ServerRequest request) {
    var reviewId = request.pathVariable("id");
    var existingMovieReview = movieReviewRepository.findById(reviewId);

    return existingMovieReview
        .flatMap(review -> movieReviewRepository.deleteById(reviewId)
            .then(ServerResponse.noContent().build()));
  }
}
