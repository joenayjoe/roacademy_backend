package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChapterRequest {

  @NotBlank(message = "{NotBlank.Chapter.name}")
  private String name;

  private int position;

  @NotNull(message = "{NotNull.Chapter.courseId}")
  private Long courseId;
}
