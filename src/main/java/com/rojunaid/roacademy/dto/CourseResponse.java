package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.CourseObjective;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CourseResponse {

  private Long id;
  private String name;
  private String headline;
  private String description;
  private Long gradeId;
  private Long hits;
  private Set<CourseObjective> objectives = new HashSet<>();
}
