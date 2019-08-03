package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChapterDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String name;

  @NotNull(message = "{NotNull.field}")
  private Long courseId;

  private Set<String> tagNames = new HashSet<>();
}
