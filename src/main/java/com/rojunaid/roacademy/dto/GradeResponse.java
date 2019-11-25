package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GradeResponse {

  private Long id;
  private String name;
  private Long categoryId;
  private List<CourseResponse> courses = new ArrayList<>();
}
