package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Tag extends Auditable {

  @JsonBackReference
  @ManyToMany(mappedBy = "tags")
  Set<Lecture> lectures = new HashSet<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "tags")
  Set<TeachingResource> teachingResources = new HashSet<>();

  @NotBlank(message = "{NotBlank.Tag.name}")
  private String name;
}
