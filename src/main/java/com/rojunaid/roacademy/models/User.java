package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojunaid.roacademy.validator.ValidEmail;
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
public class User extends Auditable {

  @ManyToMany
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new HashSet<>();
  @NotBlank
  @Size(min = 1, max = 100)
  private String firstName;
  @NotBlank
  @Size(min = 1, max = 100)
  private String lastName;
  @NotBlank
  @Size(min = 3, max = 100)
  @Column(unique = true)
  @ValidEmail
  private String email;
  @NotBlank
  @Size(min = 8, max = 100)
  @JsonIgnore
  private String hashPassword;
}
