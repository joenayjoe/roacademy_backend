package com.rojunaid.roacademy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class MediaTypeNotSupportedException extends RuntimeException {

  public MediaTypeNotSupportedException() {}

  public MediaTypeNotSupportedException(String message) {
    super(message);
  }

  public MediaTypeNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }
}
