package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrimaryCategory {
  @NotNull(message = "{NotNull.Category.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.Category.name}")
  private String name;
}
