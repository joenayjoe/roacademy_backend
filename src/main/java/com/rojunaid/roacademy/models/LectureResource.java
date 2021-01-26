package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class LectureResource extends Auditable {

  private String resourceId;
  private String fileName;
  private String contentType;
  private Long fileSize;
  private String fileUrl;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;
}
