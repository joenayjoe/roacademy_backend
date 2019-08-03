package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class GradeDTO {

  @NotBlank(message = "{NotBlank.field}")
  @Size(min = 2, max = 100, message = "{Size.field}")
  private String name;
}
