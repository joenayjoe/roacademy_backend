package com.rojunaid.roacademy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

  public OAuth2AuthenticationProcessingException(String message) {
    super(message);
  }
}
