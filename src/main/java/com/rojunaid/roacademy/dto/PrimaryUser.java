package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrimaryUser {
  @NotNull(message = "{NotNull.User.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.User.firstName}")
  private String firstName;

  @NotBlank(message = "{NotBlank.User.lastName}")
  private String lastName;

  @NotBlank(message = "{NotBlank.User.email}")
  private String email;
}
