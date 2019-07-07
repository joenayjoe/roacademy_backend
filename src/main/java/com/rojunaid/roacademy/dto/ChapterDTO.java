package com.rojunaid.roacademy.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class ChapterDTO {

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull private Long courseId;

  private Set<String> tagNames = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Set<String> getTagNames() {
    return tagNames;
  }

  public void setTagNames(Set<String> tagNames) {
    this.tagNames = tagNames;
  }
}
