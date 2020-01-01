package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class GradeUpdateRequest {

  @NotNull(message = "{NotNull.field}")
  private Long id;

  @NotBlank(message = "{NotBlank.field}")
  @Size(min = 2, max = 100, message = "{Size.field}")
  private String name;

  @NotNull(message = "{NotNull.field}")
  private Long categoryId;
}
