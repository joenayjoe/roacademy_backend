package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
