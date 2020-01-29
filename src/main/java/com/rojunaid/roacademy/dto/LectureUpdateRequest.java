package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LectureUpdateRequest extends LectureRequest {

  @NotNull(message = "${NotNull.Lecture.id}")
  private Long id;
}
