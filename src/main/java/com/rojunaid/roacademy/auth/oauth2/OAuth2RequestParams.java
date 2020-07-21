package com.rojunaid.roacademy.auth.oauth2;

import lombok.Data;

@Data
public class OAuth2RequestParams {

  private String clientId;
  private String clientSecret;
  private String codeOrToken;
  private String grantType;
  private String authOrTokenUrl;
  private String redirectUrl;
}
