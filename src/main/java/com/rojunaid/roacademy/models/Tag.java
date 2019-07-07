package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag extends Auditable {

  @ManyToMany(mappedBy = "tags")
  @JsonBackReference
  Set<Chapter> chapters = new HashSet<>();
  @NotNull private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Chapter> getChapters() {
    return chapters;
  }

  public void setChapters(Set<Chapter> chapters) {
    this.chapters = chapters;
  }
}
