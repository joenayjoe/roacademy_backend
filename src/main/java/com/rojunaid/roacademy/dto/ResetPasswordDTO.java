package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldMatch(first = "newPassword", second = "confirmPassword")
public class ResetPasswordDTO {

  @NotBlank private String oldPassword;

  @NotBlank private String newPassword;

  @NotBlank private String confirmPassword;
}
