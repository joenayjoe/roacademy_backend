package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse extends AuditableDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
  private List<RoleResponse> roles = new ArrayList<>();
}
