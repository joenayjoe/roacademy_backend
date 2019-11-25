package com.rojunaid.roacademy.models;

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
public class Category extends Auditable {

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @OrderBy("name asc")
  Set<Grade> grades = new HashSet<>();

  @NotBlank(message = "{NotBlank.field}")
  @Size(min = 2, max = 100, message = "{Size.field}")
  @Column(unique = true)
  private String name;
}
