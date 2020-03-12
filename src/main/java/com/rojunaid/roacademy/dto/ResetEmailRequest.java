package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetEmailRequest {
  @NotBlank(message = "{NotNull.User.email}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;
}
