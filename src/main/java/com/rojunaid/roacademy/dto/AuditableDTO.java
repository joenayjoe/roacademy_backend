package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditableDTO {

  private Date createdAt;

  private Date updatedAt;
}
