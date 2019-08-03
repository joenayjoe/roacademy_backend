package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String email;

  @NotBlank(message = "{NotBlank.field}")
  private String password;
}
