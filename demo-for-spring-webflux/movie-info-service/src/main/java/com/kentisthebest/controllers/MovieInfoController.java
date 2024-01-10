package com.kentisthebest.controllers;

import com.kentisthebest.models.MovieInfo;
import com.kentisthebest.services.MovieInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MovieInfoController {

  private final MovieInfoService movieInfoService;

  public MovieInfoController(MovieInfoService movieInfoService) {
    this.movieInfoService = movieInfoService;
  }

  @GetMapping(path = "/movie-info")
  public Flux<MovieInfo> getAllMovieInfo() {
    return movieInfoService.getAllMovieInfo().log();
  }

  @GetMapping(path = "/movie-info/{id}")
  public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
    return movieInfoService.getMovieInfoById(id)
        .map(movieInfo -> ResponseEntity.ok().body(movieInfo))
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
        .log();
  }

  @PostMapping(path = "/movie-info")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
    return movieInfoService.addMovieInfo(movieInfo).log();
  }

  @PutMapping(path = "/movie-info/{id}")
  public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updateMovieInfo, @PathVariable String id) {
    return movieInfoService.updateMovieInfo(updateMovieInfo, id)
        .map(movieInfo -> ResponseEntity.ok().body(movieInfo))
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
        .log();
  }

  @DeleteMapping(path = "/movie-info/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovieInfo(@PathVariable String id) {
    return movieInfoService.deleteMovieInfo(id).log();
  }

}
