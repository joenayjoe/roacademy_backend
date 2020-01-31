package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChapterPositionUpdateRequest {

  @NotNull(message = "{NotNull.Chapter.id}")
  private Long chapterId;

  @NotNull(message = "{NotNull.Chapter.position}")
  private int position;
}
