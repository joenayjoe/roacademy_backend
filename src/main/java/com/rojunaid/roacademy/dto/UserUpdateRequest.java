package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.validator.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserUpdateRequest {

  @NotNull(message = "{NotNull.User.id}")
  private Long id;

  @NotBlank(message = "{NotBlank.User.firstName}")
  private String firstName;

  @NotBlank(message = "{NotBlank.User.lastName}")
  private String lastName;

  @NotBlank(message = "{NotBlank.User.email}")
  @ValidEmail(message = "{ValidEmail.email}")
  private String email;

  private List<Long> roleIds = new ArrayList<>();
}
