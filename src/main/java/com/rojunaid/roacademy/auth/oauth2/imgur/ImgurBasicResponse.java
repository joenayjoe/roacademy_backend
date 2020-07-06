package com.rojunaid.roacademy.auth.oauth2.imgur;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ImgurBasicResponse {

  @Key("data")
  private ImgurImageResponse data;
}
