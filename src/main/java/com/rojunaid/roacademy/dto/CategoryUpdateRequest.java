package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryUpdateRequest extends CategoryRequest {

  @NotNull(message = "{NotNull.Category.id}")
  private Long id;
}
