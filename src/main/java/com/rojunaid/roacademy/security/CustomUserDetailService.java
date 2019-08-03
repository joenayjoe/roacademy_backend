package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

  @Autowired UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userService.findByEmail(email);
    return new CustomUserPrincipal(user);
  }

  public UserDetails loadUserById(Long userId) {
    User user = userService.findUserById(userId);
    return new CustomUserPrincipal(user);
  }
}
