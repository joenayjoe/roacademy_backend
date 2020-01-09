package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryRequest {
  @NotBlank(message = "{NotBlank.Category.name}")
  private String name;
}
