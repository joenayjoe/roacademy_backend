package com.rojunaid.roacademy.dto;

import lombok.Data;

@Data
public class CourseSubscriptionCheckResponse {

  private Long courseId;
  private Long studentId;
  private boolean subscribed;
}
