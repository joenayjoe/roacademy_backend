package com.rojunaid.roacademy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DirectoryCreationException extends RuntimeException {
  public DirectoryCreationException() {}

  public DirectoryCreationException(String message) {
    super(message);
  }

  public DirectoryCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
