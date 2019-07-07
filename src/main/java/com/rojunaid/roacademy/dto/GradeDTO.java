package com.rojunaid.roacademy.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GradeDTO {

  @NotNull
  @Size(min = 2, max = 100)
  @Column(unique = true)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
