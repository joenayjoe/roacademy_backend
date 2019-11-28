package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryResponse extends AuditableDTO{
  private Long id;
  private String name;
  private List<GradeResponse> grades = new ArrayList<>();
}
