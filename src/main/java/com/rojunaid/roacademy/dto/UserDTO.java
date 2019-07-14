package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.FieldMatch;
import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "password must match")
public class UserDTO {

  @NotBlank private String firstName;

  @NotBlank private String lastName;

  @ValidEmail @NotBlank private String email;

  @NotBlank private String password;

  @NotBlank private String confirmPassword;
}
