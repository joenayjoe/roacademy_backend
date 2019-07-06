package com.rojunaid.roacademy.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class CourseDTO {

  @NotEmpty
  @Column(unique = true)
  private String name;

  private Long gradeId;
  private List<Long> preRequisiteCourseIds = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public List<Long> getPreRequisiteCourseIds() {
    return preRequisiteCourseIds;
  }

  public void setPreRequisiteCourseIds(List<Long> preRequisiteCourseIds) {
    this.preRequisiteCourseIds = preRequisiteCourseIds;
  }
}
