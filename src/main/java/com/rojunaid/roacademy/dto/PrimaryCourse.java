package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrimaryCourse {
  @NotNull(message = "{NotNull.Course.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.Course.name}")
  private String name;
}
