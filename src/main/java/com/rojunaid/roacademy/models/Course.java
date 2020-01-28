package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Course extends Auditable {

  @Column(unique = true)
  private String name;

  private String headline;

  private String description;

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.LAZY)
  private Grade grade;

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  private Long hits = 0L;

  @Enumerated(EnumType.STRING)
  private LevelEnum level = LevelEnum.BEGINNER;

  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private CourseStatusEnum status = CourseStatusEnum.DRAFT;

  @JsonBackReference
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<Chapter> chapters = new HashSet<>();

  @JsonManagedReference
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<CourseObjective> objectives = new HashSet<>();

  @JsonManagedReference
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<CourseRequirement> courseRequirements = new HashSet<>();

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_instructor",
      joinColumns = {@JoinColumn(name = "course_id")},
      inverseJoinColumns = {@JoinColumn(name = "instructor_id")})
  private Set<User> instructors = new HashSet<>();

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_student",
      joinColumns = {@JoinColumn(name = "course_id")},
      inverseJoinColumns = {@JoinColumn(name = "student_id")})
  private Set<User> students = new HashSet<>();

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by", updatable = false)
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
