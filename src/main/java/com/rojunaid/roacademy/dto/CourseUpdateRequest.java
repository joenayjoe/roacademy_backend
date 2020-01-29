package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CourseUpdateRequest extends CourseRequest {
  @NotNull(message = "{NotNull.Course.id}")
  private Long id;
}
