package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChapterDTO {

  @NotBlank
  @Column(unique = true)
  private String name;

  @NotBlank private Long courseId;

  private Set<String> tagNames = new HashSet<>();
}
