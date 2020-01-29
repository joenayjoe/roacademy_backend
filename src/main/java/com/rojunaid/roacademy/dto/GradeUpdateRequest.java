package com.rojunaid.roacademy.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GradeUpdateRequest extends GradeRequest {

  @NotNull(message = "{NotNull.Grade.id}")
  private Long id;
}
