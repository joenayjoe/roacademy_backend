package com.rojunaid.roacademy.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private final Auth auth = new Auth();
  private final OAuth2 oauth2 = new OAuth2();

  public Auth getAuth() {
    return auth;
  }

  public OAuth2 getOauth2() {
    return oauth2;
  }

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

    private Box box = new Box();

    private Youtube youtube = new Youtube();

    private Imgur imgur = new Imgur();

    public List<String> getAuthorizedRedirectUris() {
      return this.authorizedRedirectUris;
    }

    public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
      this.authorizedRedirectUris = authorizedRedirectUris;
    }

    public Box getBox() {
      return box;
    }

    public void setBox(Box box) {
      this.box = box;
    }

    public Youtube getYoutube() {
      return youtube;
    }

    public void setYoutube(Youtube youtube) {
      this.youtube = youtube;
    }

    public Imgur getImgur() {
      return imgur;
    }

    public void setImgur(Imgur imgur) {
      this.imgur = imgur;
    }

    public static class Box {
      private String clientId;
      private String clientSecret;
      private String authorizationUri;
      private String tokenUri;

      public String getClientId() {
        return this.clientId;
      }

      public void setClientId(String clientId) {
        this.clientId = clientId;
      }

      public String getClientSecret() {
        return clientSecret;
      }

      public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
      }

      public String getAuthorizationUri() {
        return authorizationUri;
      }

      public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
      }

      public String getTokenUri() {
        return tokenUri;
      }

      public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
      }
    }

    public static class Youtube {
      private String clientId;
      private String clientSecret;
      private String authorizationUri;
      private String tokenUri;
      private List<String> scopes;
      private String redirectUri;

      public String getClientId() {
        return clientId;
      }

      public void setClientId(String clientId) {
        this.clientId = clientId;
      }

      public String getClientSecret() {
        return clientSecret;
      }

      public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
      }

      public String getAuthorizationUri() {
        return authorizationUri;
      }

      public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
      }

      public String getTokenUri() {
        return tokenUri;
      }

      public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
      }

      public List<String> getScopes() {
        return this.scopes;
      }

      public void setScopes(List<String> scopes) {
        this.scopes = scopes;
      }

      public String getRedirectUri() {
        return this.redirectUri;
      }

      public void setRedirectUri(String uri) {
        this.redirectUri = uri;
      }
    }

    public static class Imgur {
      private String clientId;
      private String clientSecret;
      private String authorizationUri;
      private String tokenUri;

      public String getClientId() {
        return clientId;
      }

      public void setClientId(String clientId) {
        this.clientId = clientId;
      }

      public String getClientSecret() {
        return clientSecret;
      }

      public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
      }

      public String getAuthorizationUri() {
        return authorizationUri;
      }

      public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
      }

      public String getTokenUri() {
        return tokenUri;
      }

      public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
      }
    }
  }
}
