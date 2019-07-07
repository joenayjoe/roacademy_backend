package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category extends Auditable {

  @OneToMany(mappedBy = "category")
  @JsonBackReference
  Set<Grade> grades = new HashSet<>();

  @NotNull
  @Size(min = 2, max = 100)
  @Column(unique = true)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Grade> getGrades() {
    return grades;
  }

  public void setGrades(Set<Grade> grades) {
    this.grades = grades;
  }
}
