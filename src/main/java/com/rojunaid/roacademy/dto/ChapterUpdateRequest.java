package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChapterUpdateRequest extends ChapterRequest {

  @NotNull(message = "{NotNull.Chapter.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.Chapter.name}")
  private String name;

  @NotNull(message = "{NotNull.Chapter.courseId}")
  private Long courseId;
}
