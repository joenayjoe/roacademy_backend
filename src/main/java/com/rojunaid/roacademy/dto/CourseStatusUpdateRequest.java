package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.CourseStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseStatusUpdateRequest {

  @NotNull(message = "${NotNull.Course.id}")
  private Long id;

  @NotNull(message = "${NotNull.Course.status}")
  private CourseStatusEnum status;
}
