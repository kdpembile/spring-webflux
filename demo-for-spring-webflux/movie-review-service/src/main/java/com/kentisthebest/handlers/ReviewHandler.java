package com.kentisthebest.handlers;

import com.kentisthebest.domains.Review;
import com.kentisthebest.repositories.MovieReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

  private final MovieReviewRepository movieReviewRepository;

  public ReviewHandler(MovieReviewRepository movieReviewRepository) {
    this.movieReviewRepository = movieReviewRepository;
  }

  public Mono<ServerResponse> addReview(ServerRequest request) {
    return request.bodyToMono(Review.class)
        .flatMap(movieReviewRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
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
    var existingMovieReview = movieReviewRepository.findById(reviewId);

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
        );
  }

  public Mono<ServerResponse> deleteReview(ServerRequest request) {
    var reviewId = request.pathVariable("id");
    var existingMovieReview = movieReviewRepository.findById(reviewId);

    return existingMovieReview
        .flatMap(review -> movieReviewRepository.deleteById(reviewId)
            .then(ServerResponse.noContent().build()));
  }
}
