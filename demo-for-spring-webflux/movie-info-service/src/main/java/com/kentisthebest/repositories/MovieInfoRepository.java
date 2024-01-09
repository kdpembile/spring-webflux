package com.kentisthebest.repositories;

import com.kentisthebest.models.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

}
