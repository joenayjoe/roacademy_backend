package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeachingResourceResponse {

  List<TagResponse> tags = new ArrayList<>();
  private Long id;
  private String title;
  private String description;
  private String fileName;
  private String contentType;
  private Long fileSize;
  private String hostingType;
  private String privacyStatus;
  private Long resourceOwnerId;
  private String resourceOwnerType;
  private Long uploaderId;
}
