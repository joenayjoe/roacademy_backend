package com.rojunaid.roacademy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class YoutubeUploadException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public YoutubeUploadException() {}

  public YoutubeUploadException(String message) {
    super(message);
  }

  public YoutubeUploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
