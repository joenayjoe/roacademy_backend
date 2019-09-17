package com.rojunaid.roacademy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

interface IAuthenticationFacade {
  Authentication getAuthentication();
}

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
