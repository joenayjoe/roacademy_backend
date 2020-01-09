package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "{FieldMatch.password}")
public class UserRequest {

  @NotBlank(message = "{NotBlank.User.firstName}")
  private String firstName;

  @NotBlank(message = "{NotBlank.User.lastName}")
  private String lastName;

  @ValidEmail(message = "{ValidEmail.email}")
  @NotBlank(message = "{NotBlank.User.email}")
  private String email;

  @NotBlank(message = "{NotBlank.User.password}")
  private String password;

  @NotBlank(message = "{NotBlank.User.confirmPassword}")
  private String confirmPassword;
}
