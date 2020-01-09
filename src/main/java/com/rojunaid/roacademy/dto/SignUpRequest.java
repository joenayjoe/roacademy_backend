package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "{FieldMatch.password}")
public class SignUpRequest {

  @NotBlank(message = "{NotBlank.User.firstName}")
  private String firstName;

  @NotBlank(message = "{NotBlank.User.lastName}")
  private String lastName;

  @NotBlank(message = "{NotBlank.User.email}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  @NotBlank(message = "{NotBlank.User.password}")
  private String password;

  @NotBlank(message = "{NotBlank.User.confirmPassword}")
  private String confirmPassword;

  private Long roleId;
}
