package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "{FieldMatch.password}")
public class SignUpRequest {

  @NotBlank(message = "{NotBlank.field}")
  private String firstName;

  @NotBlank(message = "{NotBlank.field}")
  private String lastName;

  @NotBlank(message = "{NotBlank.field}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  @NotBlank(message = "{NotBlank.field}")
  private String password;

  @NotBlank(message = "{NotBlank.field}")
  private String confirmPassword;

  private Long roleId;
}
