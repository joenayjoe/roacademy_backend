package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmPassword", message = "{FieldMatch.password}")
public class ResetPasswordDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String oldPassword;

  @NotBlank(message = "{NotBlank.field}")
  private String newPassword;

  @NotBlank(message = "{NotBlank.field}") private String confirmPassword;
}
