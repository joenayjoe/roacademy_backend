package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class GradeRequest {

  @NotBlank(message = "{NotBlank.Grade.name}")
  @Size(min = 2, max = 100, message = "{Size.Grade.name}")
  private String name;

  @NotNull(message = "{NotNull.Grade.categoryId}")
  private Long categoryId;
}
