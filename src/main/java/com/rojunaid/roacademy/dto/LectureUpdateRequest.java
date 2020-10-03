package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class LectureUpdateRequest {

  @NotNull(message = "${NotNull.Lecture.id}")
  private Long id;

  @NotBlank(message = "${NotBlank.Lecture.name}")
  private String name;

  @NotBlank(message = "${NotBlank.Lecture.description}")
  private String description;

  @NotNull(message = "${NotNull.Lecture.chapterId}")
  private Long chapterId;

  private List<String> tags = new ArrayList<>();
}
