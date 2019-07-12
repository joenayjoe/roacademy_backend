package com.rojunaid.roacademy.dto.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError {

  private String code;
  private String message;
}
