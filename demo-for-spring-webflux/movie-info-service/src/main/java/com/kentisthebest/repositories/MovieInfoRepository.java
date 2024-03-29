package com.kentisthebest.repositories;

import com.kentisthebest.models.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

  Flux<MovieInfo> findByYear(Integer year);

  Mono<MovieInfo> findByName(String name);
}
