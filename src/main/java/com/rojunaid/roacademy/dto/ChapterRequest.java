package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChapterRequest {

  @NotBlank(message = "{NotBlank.field}")
  private String name;

  @NotNull(message = "{NotNull.field}")
  private Long courseId;

  private Set<String> tagNames = new HashSet<>();
}
