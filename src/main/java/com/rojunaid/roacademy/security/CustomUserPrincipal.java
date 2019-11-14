package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomUserPrincipal implements UserDetails, OAuth2User {

  private User user;
  private Map<String, Object> attributes;

  public CustomUserPrincipal(User user) {
    this.user = user;
  }

  public CustomUserPrincipal(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  public static CustomUserPrincipal create(User user) {
    return new CustomUserPrincipal(user);
  }

  public static CustomUserPrincipal create(User user, Map<String, Object> attributes) {
    return new CustomUserPrincipal(user, attributes);
  }

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

  @Override
  public String getName() {
    return user.getId().toString();
  }

  //  public User getUser() {
  //    return user;
  //  }
  //
  //  public Map<String, Object> getAttributes() {
  //    return attributes;
  //  }

}
