package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Chapter extends Auditable {

  @ManyToMany
  @JoinTable(
      name = "chapter_tag",
      joinColumns = @JoinColumn(name = "chapter_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  @NotBlank(message = "{NotBlank.field}")
  private String name;

  @JsonBackReference @ManyToOne private Course course;
}
