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
public class User extends Auditable {

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
  @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
  Set<Course> enrolledCourses = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
  private Set<Course> createdCourses = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
  private Set<Lecture> createdLectures = new HashSet<>();

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
