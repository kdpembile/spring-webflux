package com.kentisthebest.exceptions.handlers;

import com.kentisthebest.exceptions.ReviewDataException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    log.error("Exception message is {} ", ex.getMessage(), ex);
    DataBufferFactory dataBufferFactory  = exchange.getResponse().bufferFactory();
    var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());

    if (ex instanceof ReviewDataException) {
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

      return  exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
    return null;
  }
}
