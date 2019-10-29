package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmPassword", message = "{FieldMatch.password}")
public class ResetPasswordRequest {

  @NotBlank(message = "{NotBlank.field}")
  private String oldPassword;

  @NotBlank(message = "{NotBlank.field}")
  private String newPassword;

  @NotBlank(message = "{NotBlank.field}")
  private String confirmPassword;
}
