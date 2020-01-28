package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Grade extends Auditable {

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.LAZY)
  Category category;

  @Column(unique = true)
  private String name;

  @JsonBackReference
  @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
  private Set<Course> courses = new HashSet<>();
}
