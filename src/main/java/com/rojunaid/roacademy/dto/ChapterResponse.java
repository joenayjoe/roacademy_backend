package com.rojunaid.roacademy.dto;

import com.rojunaid.roacademy.models.TeachingResource;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChapterResponse {
  private Long id;
  private String name;
  private Long courseId;
  private String url;
  List<TagResponse> tags = new ArrayList<>();
  List<TeachingResource> teachingResources = new ArrayList<>();
}
