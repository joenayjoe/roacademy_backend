package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Chapter extends Auditable {

  private String name;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "chapter",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  private Set<Lecture> lectures = new HashSet<>();

  @JsonManagedReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  @EqualsAndHashCode.Exclude
  private Course course;
}
