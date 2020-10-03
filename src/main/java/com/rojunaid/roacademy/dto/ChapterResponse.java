package com.rojunaid.roacademy.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterResponse extends AuditableDTO {
  List<LectureResponse> lectures = new ArrayList<>();
  private Long id;
  private String name;
  private PrimaryCourse primaryCourse;
  private int position;
}
