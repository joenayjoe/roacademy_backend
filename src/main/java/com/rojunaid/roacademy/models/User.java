package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "app_user")
public class User extends Auditable {

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY, orphanRemoval = true)
  Set<CourseInstructor> courseInstructors = new HashSet<>();

  @OneToMany(
      mappedBy = "student",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  Set<CourseStudent> courseStudents = new HashSet<>();

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String email;

  @JsonIgnore private String hashPassword;

  private String imageUrl;

  private String imageId;

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private Boolean enable;

  @JsonIgnore
  @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
  private Set<Course> createdCourses = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
  private Set<Lecture> createdLectures = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "commentedBy", fetch = FetchType.LAZY)
  private Set<CourseComment> courseComments = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "commentedBy", fetch = FetchType.LAZY)
  private Set<LectureComment> lectureComments = new HashSet<>();

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public void subscribeCourse(Course course) {
    CourseStudent courseStudent = new CourseStudent();
    courseStudent.setStudent(this);
    courseStudent.setCourse(course);

    course.getCourseStudents().add(courseStudent);
    courseStudents.add(courseStudent);
  }

  public void unsubscribe(Course course, CourseStudent courseStudent) {
    course.getCourseStudents().remove(courseStudent);
    courseStudents.remove(courseStudent);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User that = (User) o;

    return Objects.equals(this.getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }
}
