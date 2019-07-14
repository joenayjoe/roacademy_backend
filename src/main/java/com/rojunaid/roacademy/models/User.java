package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class User extends Auditable {

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
