package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ChapterResponse extends AuditableDTO {
  private Long id;
  private String name;
  private PrimaryCourse primaryCourse;
  private int position;
  List<LectureResponse> lectures = new ArrayList<>();
}
