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

  @NotBlank(message = "First Name: {NotBlank.field}")
  @Size(min = 1, max = 100, message = "{Size.field}")
  private String firstName;

  @NotBlank(message = "Last Name: {NotBlank.field}")
  @Size(min = 1, max = 100, message = "{Size.field}")
  private String lastName;

  @NotBlank(message = "Email: {NotBlank.field}")
  @Size(min = 3, max = 100, message = "{Size.field}")
  @Column(unique = true)
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  @NotBlank(message = "Password: {NotBlank.field}")
  @Size(min = 8, max = 100, message = "{Size.field}")
  @JsonIgnore
  private String hashPassword;

  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private Boolean enable;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  Set<TeachingResource> teachingResources = new HashSet<>();
}
