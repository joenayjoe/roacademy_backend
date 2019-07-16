package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role extends Auditable {

  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  Set<User> users = new HashSet<>();

  private String name;
}
