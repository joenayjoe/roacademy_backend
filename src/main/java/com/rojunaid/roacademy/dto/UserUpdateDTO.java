package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserUpdateDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String firstName;

  @NotBlank(message = "{NotBlank.field}")
  private String lastName;

  @NotBlank(message = "{NotBlank.field}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;
}
