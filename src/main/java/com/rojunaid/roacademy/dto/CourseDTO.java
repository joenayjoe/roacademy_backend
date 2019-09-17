package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String name;

  private Long gradeId;
  private List<Long> preRequisiteCourseIds = new ArrayList<>();
}
