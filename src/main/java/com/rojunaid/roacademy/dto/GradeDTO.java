package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class GradeDTO {

  @NotNull
  @Size(min = 2, max = 100)
  @Column(unique = true)
  private String name;
}
