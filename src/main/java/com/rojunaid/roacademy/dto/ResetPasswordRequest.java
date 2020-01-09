package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmPassword", message = "{FieldMatch.password}")
public class ResetPasswordRequest {

  @NotBlank(message = "{NotBlank.User.oldPassword}")
  private String oldPassword;

  @NotBlank(message = "{NotBlank.User.newPassword}")
  @Size(min = 8, max = 100, message = "{Size.User.newPassword}")
  private String newPassword;

  @NotBlank(message = "{NotBlank.User.confirmPassword}")
  private String confirmPassword;
}
