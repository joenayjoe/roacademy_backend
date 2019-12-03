package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.models.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
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

  private Long categoryId;

  @NotNull(message = "{NotNull.field}")
  private Long gradeId;

  @NotNull(message = "{NotNull.field}")
  @Enumerated(EnumType.STRING)
  private LevelEnum level;

  private List<Long> preRequisiteCourseIds = new ArrayList<>();

  private List<String> objectives = new ArrayList<>();

  private List<String> requirements = new ArrayList<>();
}
