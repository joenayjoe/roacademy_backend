package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {

  @NotBlank(message = "{NotBlank.CourseComment.commentBody}")
  private String commentBody;
}
