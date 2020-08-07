package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentUpdateRequest extends CommentRequest {
  @NotNull(message = "{NotNull.Course.id}")
  private Long id;
}
