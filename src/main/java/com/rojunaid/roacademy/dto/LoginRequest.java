package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

  @NotBlank(message = "{NotBlank.User.email}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  @NotBlank(message = "{NotBlank.User.password}")
  private String password;
}
