package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Chapter extends Auditable {

  @ManyToMany
  @JoinTable(
      name = "chapter_tag",
      joinColumns = @JoinColumn(name = "chapter_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  Set<Tag> tags = new HashSet<>();
  @NotEmpty private String name;
  @ManyToOne @JsonBackReference private Course course;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
}
