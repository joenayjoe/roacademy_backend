package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

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
  private String headline;

  @NotBlank(message = "{NotBlank.field}")
  private String description;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private Grade grade;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  private Long hits;

  @Enumerated(EnumType.STRING)
  private LevelEnum level = LevelEnum.BEGINNER;

  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private CourseStatusEnum status = CourseStatusEnum.DRAFT;

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

  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<Chapter> chapters = new HashSet<>();

  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<CourseObjective> objectives = new HashSet<>();

  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<CourseRequirement> courseRequirements = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_instructor",
      joinColumns = {@JoinColumn(name = "course_id")},
      inverseJoinColumns = {@JoinColumn(name = "instructor_id")})
  private Set<User> instructors = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_student",
      joinColumns = {@JoinColumn(name = "course_id")},
      inverseJoinColumns = {@JoinColumn(name = "student_id")})
  private Set<User> students = new HashSet<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by")
  private User createdBy;

  public void addCategory(Category category) {
    this.setCategory(category);
    category.getCourses().add(this);
  }

  public void addGrade(Grade grade) {
    this.setGrade(grade);
    grade.getCourses().add(this);
  }

  public void addCourseObjective(CourseObjective objective) {
    this.getObjectives().add(objective);
    objective.setCourse(this);
  }

  public void addCourseRequirement(CourseRequirement requirement) {
    this.getCourseRequirements().add(requirement);
    requirement.setCourse(this);
  }

  public void addChapter(Chapter chapter) {
    this.getChapters().add(chapter);
    chapter.setCourse(this);
  }

  @PrePersist
  public void addCreatedBy() {
    CustomUserPrincipal principal =
        (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = principal.getUser();
    this.setCreatedBy(user);
  }
}
