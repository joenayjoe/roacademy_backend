package com.rojunaid.roacademy.models;

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

  @ManyToMany(mappedBy = "tags")
  @JsonIgnore
  Set<Chapter> chapters = new HashSet<>();
  @ManyToMany(mappedBy = "tags")
  @JsonIgnore
  Set<TeachingResource> teachingResources = new HashSet<>();
  @NotBlank(message = "{NotBlank.field}")
  private String name;
}
