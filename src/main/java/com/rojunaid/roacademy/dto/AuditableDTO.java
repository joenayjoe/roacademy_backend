package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditableDTO {

  // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM YYYY 'at' hh:mm:ss a")
  private Date createdAt;

  // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM YYYY 'at' hh:mm:ss a")
  private Date updatedAt;
}
