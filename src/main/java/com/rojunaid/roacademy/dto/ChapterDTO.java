package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChapterDTO {

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull private Long courseId;

  private Set<String> tagNames = new HashSet<>();
}
