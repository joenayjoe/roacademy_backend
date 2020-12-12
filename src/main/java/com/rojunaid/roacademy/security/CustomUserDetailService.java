package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

  @Autowired UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userRepository.findByEmail(email).orElse(null);
    if (user == null) {
      throw new ResourceNotFoundException("incorrect credentials");
    }
    return new CustomUserPrincipal(user);
  }

  public UserDetails loadUserById(Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new ResourceNotFoundException("User with ID [" + userId + "] not found");
    }
    return new CustomUserPrincipal(user);
  }
}
