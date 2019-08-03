package com.rojunaid.roacademy.youtube;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import com.google.common.reflect.TypeToken;
import com.rojunaid.roacademy.auth.oauth2.OAuth2CredentialService;
import com.rojunaid.roacademy.models.OAuth2Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class Auth {

  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  public static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";

  private OAuth2CredentialService oAuth2CredentialService;

  public Auth() {}

  @Autowired
  public Auth(OAuth2CredentialService service) {
    this.oAuth2CredentialService = service;
  }

  public Credential authorize() {

    OAuth2Credential credential = oAuth2CredentialService.getYoutubeCredential();

    if (!isValidToken(credential)) {
      // refresh access token
      try {

        HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                  request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        GenericUrl url = new GenericUrl(TOKEN_URI);

        Map<String, String> param = new HashMap<>();
        param.put("client_id", credential.getClientId());
        param.put("client_secret", credential.getClientSecret());
        param.put("refresh_token", credential.getRefreshToken());
        param.put("grant_type", "refresh_token");

        HttpContent content = new UrlEncodedContent(param);

        HttpRequest request = requestFactory.buildPostRequest(url, content);

        Type type = new TypeToken<TokenResponse>() {}.getType();
        HttpResponse response = request.execute();
        TokenResponse tokenResponse = (TokenResponse) response.parseAs(type);

        Long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        credential.setAccessToken(tokenResponse.getAccessToken());
        credential.setExpiresInSeconds(currentTimeInSeconds + tokenResponse.getExpiresInSeconds());
        credential.setScope(tokenResponse.getScope());
        credential.setTokenType(tokenResponse.getTokenType());

        credential = oAuth2CredentialService.storeYoutubeCredential(credential);

      } catch (IOException e) {
        System.out.println(e);
      }
    }

    Credential.Builder builder =
        (new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()))
            .setTransport(HTTP_TRANSPORT)
            .setJsonFactory(JSON_FACTORY)
            .setTokenServerEncodedUrl(TOKEN_URI)
            .setClientAuthentication(null)
            .setRequestInitializer(null)
            .setClock(Clock.SYSTEM);

    Credential oauth2credential = builder.build().setAccessToken(credential.getAccessToken());

    return oauth2credential;
  }

  // private method

  private boolean isValidToken(OAuth2Credential credential) {
    Long currentTimeInSeconds = System.currentTimeMillis() / 1000;
    return credential.getExpiresInSeconds() - currentTimeInSeconds > 120L ? true : false;
  }
}
