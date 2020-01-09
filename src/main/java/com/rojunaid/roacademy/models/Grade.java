package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Grade extends Auditable {

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  Category category;

  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Course> courses = new HashSet<>();
}
