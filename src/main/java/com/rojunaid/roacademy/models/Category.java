package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Category extends Auditable {

  @JsonBackReference
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @OrderBy("name asc")
  Set<Grade> grades = new HashSet<>();
  @Column(unique = true)
  private String name;
  @JsonBackReference
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private Set<Course> courses = new HashSet<>();
}
