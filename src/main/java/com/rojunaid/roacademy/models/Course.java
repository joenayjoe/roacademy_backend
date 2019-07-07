package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course extends Auditable {

  @NotEmpty
  @NotBlank
  @Size(min = 3, max = 100)
  @Column(unique = true)
  private String name;

  @ManyToOne private Grade grade;

  @ManyToMany(mappedBy = "preRequisiteCourses")
  @JsonBackReference
  private Set<Course> parentCourses = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "CourseRel",
      joinColumns = {@JoinColumn(name = "CourseId")},
      inverseJoinColumns = {@JoinColumn(name = "ParentId")})
  private Set<Course> preRequisiteCourses = new HashSet<>();

  @OneToMany(mappedBy = "course")
  private Set<Chapter> chapters = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Grade getGrade() {
    return grade;
  }

  public void setGrade(Grade grade) {
    this.grade = grade;
  }

  public Set<Course> getParentCourses() {
    return parentCourses;
  }

  public void setParentCourses(Set<Course> parentCourses) {
    this.parentCourses = parentCourses;
  }

  public Set<Course> getPreRequisiteCourses() {
    return preRequisiteCourses;
  }

  public void setPreRequisiteCourses(Set<Course> preRequisiteCourses) {
    this.preRequisiteCourses = preRequisiteCourses;
  }

  public Set<Chapter> getChapters() {
    return chapters;
  }

  public void setChapters(Set<Chapter> chapters) {
    this.chapters = chapters;
  }
}
