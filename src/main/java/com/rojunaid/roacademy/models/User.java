package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private Boolean enable;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  Set<TeachingResource> teachingResources = new HashSet<>();

  @ManyToMany(mappedBy = "instructors", fetch = FetchType.LAZY)
  Set<Course> teachingCourses = new HashSet<>();

  @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
  Set<Course> enrolledCourses = new HashSet<>();

  @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Course> createdCourses = new HashSet<>();
}
