package com.rojunaid.roacademy.dto;

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
public class CourseUpdateRequest {
  @NotNull(message = "{NotNull.Course.id}")
  private Long id;
  @NotBlank(message = "{NotBlank.Course.name}")
  private String name;

  @NotBlank(message = "{NotBlank.Course.headline}")
  private String headline;

  @NotBlank(message = "{NotBlank.Course.description}")
  private String description;

  @NotNull(message = "{NotNull.Course.categoryId}")
  private Long categoryId;

  @NotNull(message = "{NotNull.Course.gradeId}")
  private Long gradeId;

  @NotNull(message = "{NotNull.Course.level}")
  @Enumerated(EnumType.STRING)
  private LevelEnum level;

  private List<String> objectives = new ArrayList<>();

  private List<String> requirements = new ArrayList<>();
}
