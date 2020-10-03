// package com.rojunaid.roacademy.auth.oauth2.youtube;
//
// import com.google.api.client.auth.oauth2.BearerToken;
// import com.google.api.client.auth.oauth2.Credential;
// import com.google.api.client.http.HttpTransport;
// import com.google.api.client.http.javanet.NetHttpTransport;
// import com.google.api.client.json.JsonFactory;
// import com.google.api.client.json.jackson2.JacksonFactory;
// import com.google.api.client.util.Clock;
// import com.rojunaid.roacademy.auth.oauth2.OAuth2CredentialService;
// import com.rojunaid.roacademy.configs.AppProperties;
// import com.rojunaid.roacademy.exception.OAuth2AuthenticationProcessingException;
// import com.rojunaid.roacademy.models.AuthProvider;
// import com.rojunaid.roacademy.models.OAuth2Credential;
// import com.rojunaid.roacademy.util.OAuth2Utils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import java.io.IOException;
//
// @Component("YoutubeAuth")
// public class Auth {
//
//  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//  public static final JsonFactory JSON_FACTORY = new JacksonFactory();
//  @Autowired private AppProperties appProperties;
//
//  @Autowired private OAuth2CredentialService oAuth2CredentialService;
//
//  public Auth() {}
//
//  public Credential authorize() {
//
//    OAuth2Credential credential =
//        oAuth2CredentialService.getCredentialByProvider(AuthProvider.youtube.name()).orElse(null);
//
//    if (!OAuth2Utils.isValidToken(credential)) {
//
//      if (credential == null) {
//        // get access token
//
//      } else {
//        // refresh access token
//
//        try {
//          TokenResponse tokenResponse =
//              OAuth2Utils.getOrRefreshAccessToken(
//                  this.getClientId(),
//                  this.getClientSecret(),
//                  "refresh_token",
//                  credential.getRefreshToken(),
//                  this.getTokenUri());
//
//          credential.setAccessToken(tokenResponse.getAccessToken());
//          credential.setRefreshToken(tokenResponse.getRefreshToken());
//          Long currentTime = System.currentTimeMillis() / 1000;
//          credential.setExpiresInSeconds(currentTime + tokenResponse.getExpiresInSeconds());
//          credential.setProvider(AuthProvider.box.name());
//
//          oAuth2CredentialService.createOrUpdateCredential(credential);
//        } catch (IOException e) {
//          throw new OAuth2AuthenticationProcessingException(e.getLocalizedMessage());
//        }
//      }
//    }
//
//    Credential.Builder builder =
//        (new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()))
//            .setTransport(HTTP_TRANSPORT)
//            .setJsonFactory(JSON_FACTORY)
//            .setTokenServerEncodedUrl(this.getTokenUri())
//            .setClientAuthentication(null)
//            .setRequestInitializer(null)
//            .setClock(Clock.SYSTEM);
//
//    Credential oauth2credential = builder.build().setAccessToken(credential.getAccessToken());
//
//    return oauth2credential;
//  }
//
//  public String getClientId() {
//    return appProperties.getOauth2().getYoutube().getClientId();
//  }
//
//  public String getClientSecret() {
//    return appProperties.getOauth2().getYoutube().getClientSecret();
//  }
//
//  public String getAuthorizationUri() {
//    return appProperties.getOauth2().getYoutube().getAuthorizationUri();
//  }
//
//  public String getTokenUri() {
//    return appProperties.getOauth2().getYoutube().getTokenUri();
//  }
// }
