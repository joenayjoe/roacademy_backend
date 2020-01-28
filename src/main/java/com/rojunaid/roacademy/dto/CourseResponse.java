package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CourseResponse extends AuditableDTO {

  private Long id;
  private String name;
  private String headline;
  private String description;
  private PrimaryCategory primaryCategory;
  private PrimaryGrade primaryGrade;
  private Long hits;
  private String level;
  private String imageUrl;
  private String status;
  private PrimaryUser createdBy;
  private Set<PrimaryUser> instructors = new HashSet<>();
  private Set<PrimaryUser> students = new HashSet<>();
  private List<String> objectives = new ArrayList<>();
  private List<String> requirements = new ArrayList<>();
}
