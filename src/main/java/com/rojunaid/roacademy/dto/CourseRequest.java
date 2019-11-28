package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseRequest {

  @NotBlank(message = "{NotBlank.field}")
  private String name;

  @NotBlank(message = "{NotBlank.field}")
  private String headline;

  @NotBlank(message = "{NotBlank.field}")
  private String description;

  @NotNull(message = "{NotNull.field}")
  private Long gradeId;

  private List<Long> preRequisiteCourseIds = new ArrayList<>();

  private List<String> objectives = new ArrayList<>();
}
