package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GradeUpdateRequest extends GradeRequest {

  @NotNull(message = "{NotNull.Grade.id}")
  private Long id;
}
