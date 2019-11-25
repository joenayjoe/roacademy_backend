package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Course extends Auditable {

  @NotBlank(message = "{NotBlank.field}")
  @Size(min = 3, max = 100, message = "{Size.field}")
  @Column(unique = true)
  private String name;

  @NotBlank(message = "{NotBlank.field}")
  private String description;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private Grade grade;

  @ManyToMany(
      mappedBy = "preRequisiteCourses",
      cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
      fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Course> parentCourses = new HashSet<>();

  @ManyToMany(
      cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
      fetch = FetchType.LAZY)
  @JoinTable(
      name = "CourseRel",
      joinColumns = {@JoinColumn(name = "ChildCourseId")},
      inverseJoinColumns = {@JoinColumn(name = "ParentCourseId")})
  private Set<Course> preRequisiteCourses = new HashSet<>();

  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Chapter> chapters = new HashSet<>();
}
