package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseSubscriptionRequest {
  @NotNull private Long studentId;

  @NotNull private Long courseId;
}
