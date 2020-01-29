package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.LectureResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LectureResponse extends AuditableDTO {

  private Long id;
  private String name;
  private String description;
  private List<String> tags = new ArrayList<>();
  private LectureResource lectureResource;
}
