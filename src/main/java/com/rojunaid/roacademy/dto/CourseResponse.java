package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {

  private Long id;
  private String name;
  private String description;
  private Long gradeId;
}
