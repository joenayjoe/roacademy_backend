package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User extends Auditable {

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new HashSet<>();
  @JsonIgnore
  @ManyToMany(mappedBy = "instructors", fetch = FetchType.LAZY)
  Set<Course> teachingCourses = new HashSet<>();
  @JsonIgnore
  @ManyToMany(
      mappedBy = "students",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.DETACH})
  Set<Course> enrolledCourses = new HashSet<>();
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

    Set<Course> subscribedCourses = this.getEnrolledCourses();
    if (subscribedCourses.contains(course)) {
      subscribedCourses.remove(course);
      course.getStudents().remove(this);
    } else {
      subscribedCourses.add(course);
      course.getStudents().add(this);
    }
  }
}
