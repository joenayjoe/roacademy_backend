package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {

  private Long id;
  private RoleEnum name;
}
