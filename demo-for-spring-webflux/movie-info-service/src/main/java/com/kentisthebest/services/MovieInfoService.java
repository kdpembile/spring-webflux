package com.kentisthebest.services;

import com.kentisthebest.models.MovieInfo;
import com.kentisthebest.repositories.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

  private final MovieInfoRepository movieInfoRepository;

  public MovieInfoService(MovieInfoRepository movieInfoRepository) {
    this.movieInfoRepository = movieInfoRepository;
  }

  public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
    return movieInfoRepository.save(movieInfo);
  }

  public Flux<MovieInfo> getAllMovieInfo() {
    return movieInfoRepository.findAll();
  }

  public Mono<MovieInfo> getMovieInfoById(String id) {
    return movieInfoRepository.findById(id);
  }

  public Mono<MovieInfo> updateMovieInfo(MovieInfo updateMovieInfo, String id) {
    return movieInfoRepository.findById(id)
        .flatMap(movieInfo -> {
          movieInfo.setName(updateMovieInfo.getName());
          movieInfo.setYear(updateMovieInfo.getYear());
          movieInfo.setCast(updateMovieInfo.getCast());
          movieInfo.setReleaseDate(updateMovieInfo.getReleaseDate());

          return movieInfoRepository.save(movieInfo);
        });
  }

  public Mono<Void> deleteMovieInfo(String id) {
    return movieInfoRepository.deleteById(id);
  }

  public Flux<MovieInfo> getMovieInfoByYear(Integer year) {
    return movieInfoRepository.findByYear(year);
  }
}
