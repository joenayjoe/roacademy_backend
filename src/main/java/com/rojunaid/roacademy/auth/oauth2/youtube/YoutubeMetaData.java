package com.rojunaid.roacademy.auth.oauth2.youtube;

import lombok.Data;

import java.util.List;

@Data
public class YoutubeMetaData {

  private String title;
  private String description;
  private List<String> tags;
  private String status;
}
