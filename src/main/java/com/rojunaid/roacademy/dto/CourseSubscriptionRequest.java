package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseSubscriptionRequest {
  @NotNull private Long userId;

  @NotNull private Long courseId;
}
