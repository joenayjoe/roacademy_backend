package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
  private List<PrimaryUser> instructors = new ArrayList<>();
  private List<PrimaryUser> students = new ArrayList<>();
  private List<String> objectives = new ArrayList<>();
  private List<String> requirements = new ArrayList<>();
}
