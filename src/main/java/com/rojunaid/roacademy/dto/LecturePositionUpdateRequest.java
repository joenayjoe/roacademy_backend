package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LecturePositionUpdateRequest {

  @NotNull(message = "${NotNull.Lecture.chapterId}")
  private Long chapterId;

  @NotNull(message = "${NotNull.Lecture.id}")
  private Long lectureId;

  @NotNull(message = "${NotNull.Lecture.position}")
  private int position;
}
