package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private List<RoleResponse> roles = new ArrayList<>();
  private String url;
}
