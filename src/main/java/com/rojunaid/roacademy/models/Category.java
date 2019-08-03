package com.rojunaid.roacademy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Category extends Auditable {

  @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
  Set<Grade> grades = new HashSet<>();

  @NotBlank(message = "{NotBlank.field}")
  @Size(min = 2, max = 100, message = "{Size.field}")
  @Column(unique = true)
  private String name;
}
