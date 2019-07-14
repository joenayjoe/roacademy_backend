package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDTO {

  @NotBlank
  @Column(unique = true)
  private String name;

  private Long gradeId;
  private List<Long> preRequisiteCourseIds = new ArrayList<>();
}
