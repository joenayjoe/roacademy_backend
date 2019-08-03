package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "{FieldMatch.password}")
public class UserDTO {

  @NotBlank(message = "{NotBlank.field}")
  private String firstName;

  @NotBlank(message = "{NotBlank.field}")
  private String lastName;

  @ValidEmail(message = "{ValidEmail.email}")
  @NotBlank(message = "{NotBlank.field}")
  private String email;

  @NotBlank(message = "{NotBlank.field}")
  private String password;

  @NotBlank(message = "{NotBlank.field}")
  private String confirmPassword;

  @NotBlank(message = "{NotBlank.field}")
  private List<Long> roleIds;
}
