package com.rojunaid.roacademy.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorDetail {

  private String title;
  private int status;
  private String detail;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime occurredAt;

  private String developerMessage;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private Map<String, List<ValidationError>> errors = new HashMap<>();
}
