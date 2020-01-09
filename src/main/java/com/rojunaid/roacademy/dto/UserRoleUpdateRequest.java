package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRoleUpdateRequest {

  @NotBlank(message = "{NotBlank.Role.id}")
  private List<Long> roleIds = new ArrayList<>();

  @NotEmpty(message = "{NotEmpty.User.id}")
  private Long userId;
}
