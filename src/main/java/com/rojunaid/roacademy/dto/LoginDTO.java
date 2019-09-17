package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

  @NotBlank(message = "{NotBlank.field}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  @NotBlank(message = "{NotBlank.field}")
  private String password;
}
