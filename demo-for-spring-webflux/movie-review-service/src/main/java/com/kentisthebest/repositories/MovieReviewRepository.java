package com.kentisthebest.repositories;

import com.kentisthebest.domains.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieReviewRepository extends ReactiveMongoRepository<Review, String> {

  Flux<Review> findReviewsByMovieInfoId(Long movieInfoId);

}
