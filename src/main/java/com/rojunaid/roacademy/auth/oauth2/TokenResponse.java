package com.rojunaid.roacademy.auth.oauth2;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class TokenResponse {

  @Key("access_token")
  private String accessToken;

  @Key("refresh_token")
  private String refreshToken;

  @Key private String scope;

  @Key("token_type")
  private String tokenType;

  @Key("expires_in")
  private Long expiresInSeconds;
}
