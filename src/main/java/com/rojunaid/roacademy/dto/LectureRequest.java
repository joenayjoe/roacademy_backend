package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.Tag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class LectureRequest {

  @NotBlank(message = "${NotBlank.Lecture.name}")
  private String name;

  @NotBlank(message = "${NotBlank.Lecture.description}")
  private String description;

  @NotNull(message = "${NotNull.Lecture.lectureId}")
  private Long chapterId;

  private Set<String> tags = new HashSet<>();
}
