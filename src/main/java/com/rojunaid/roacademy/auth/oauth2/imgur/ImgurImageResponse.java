package com.rojunaid.roacademy.auth.oauth2.imgur;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ImgurImageResponse {

  @Key("id")
  private String id;

  @Key("title")
  private String title;

  @Key("name")
  private String name;

  @Key("link")
  private String imageUrl;

  @Key("deletehash")
  private String imageDeleteHash;
}
