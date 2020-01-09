package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GradeResponse extends AuditableDTO {

  private Long id;
  private String name;
  private PrimaryCategory primaryCategory;
  private List<CourseResponse> courses = new ArrayList<>();
}
