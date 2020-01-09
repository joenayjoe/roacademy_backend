package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PrimaryGrade {

  @NotNull(message = "{NotNull.Grade.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.Grade.name}")
  private String name;
}
