package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

  private String imageId;

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

  @OneToMany(
      mappedBy = "course",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<CourseInstructor> courseInstructors = new HashSet<>();

  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<CourseStudent> courseStudents = new HashSet<>();

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "created_by", updatable = false)
  private User createdBy;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "course",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<CourseComment> comments = new HashSet<>();

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

  public void addInstructor(User user) {
    CourseInstructor courseInstructor = new CourseInstructor();
    courseInstructor.setCourse(this);
    courseInstructor.setInstructor(user);

    courseInstructors.add(courseInstructor);
    user.getCourseInstructors().add(courseInstructor);
  }

  public void removeInstructor(User user, CourseInstructor courseInstructor) {
    courseInstructors.remove(courseInstructor);
    user.getCourseInstructors().remove(courseInstructor);
  }

  public void addCreator(User user) {
    this.setCreatedBy(user);
    user.getCreatedCourses().add(this);
  }

  public void addComment(CourseComment comment) {
    this.getComments().add(comment);
    comment.setCourse(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course that = (Course) o;

    return Objects.equals(this.getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId(), this.getName());
  }
}
