package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChapterResponse extends AuditableDTO {
  private Long id;
  private String name;
  private PrimaryCourse primaryCourse;
  Set<LectureResponse> lectures = new HashSet<>();
}
