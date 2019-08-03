package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CustomUserPrincipal implements UserDetails {

  private User user;

  @Override
  public List<GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority((role.getName().name())))
            .collect(Collectors.toList());
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getHashPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public User getUser() {
    return user;
  }
}
