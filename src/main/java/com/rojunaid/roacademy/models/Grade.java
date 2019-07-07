package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Grade extends Auditable {

  @NotNull
  @Size(min = 3, max = 100, message = "Please give name between 3 to 100 characters.")
  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
  @JsonBackReference
  private Set<Course> courses = new HashSet<>();

  @ManyToOne Category category;

  public Grade() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Course> getCourses() {
    return courses;
  }

  public void setCourses(Set<Course> courses) {
    this.courses = courses;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
