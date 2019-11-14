package com.rojunaid.roacademy.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private final Auth auth = new Auth();
  private final OAuth2 oauth2 = new OAuth2();

  public static class Auth {
    private String tokenSecret;
    private long tokenExpireInMsec;

    public String getTokenSecret() {
      return tokenSecret;
    }

    public void setTokenSecret(String tokne) {
      this.tokenSecret = tokne;
    }

    public long getTokenExpireInMsec() {
      return this.tokenExpireInMsec;
    }

    public void setTokenExpireInMsec(long tokenExpireInMsec) {
      this.tokenExpireInMsec = tokenExpireInMsec;
    }
  }

  public static class OAuth2 {
    private List<String> authorizedRedirectUris = new ArrayList<>();

    public List<String> getAuthorizedRedirectUris() {
      return this.authorizedRedirectUris;
    }

    public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
      this.authorizedRedirectUris = authorizedRedirectUris;
      return this;
    }
  }

  public Auth getAuth() {
    return auth;
  }

  public OAuth2 getOauth2() {
    return oauth2;
  }
}
