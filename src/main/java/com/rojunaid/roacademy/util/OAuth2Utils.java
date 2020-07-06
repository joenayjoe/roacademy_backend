package com.rojunaid.roacademy.util;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.reflect.TypeToken;
import com.rojunaid.roacademy.auth.oauth2.Auth;
import com.rojunaid.roacademy.auth.oauth2.TokenResponse;
import com.rojunaid.roacademy.models.OAuth2Credential;
import lombok.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OAuth2Utils {

  public static TokenResponse getOrRefreshAccessToken(
      String clientId,
      String clientSecret,
      String grant_type,
      String tokenOrCode,
      String authOrTokenUri)
      throws IOException {

    HttpRequestFactory requestFactory =
        HttpClientUtils.HTTP_TRANSPORT.createRequestFactory(
            (HttpRequest request) -> {
              request.setParser(new JsonObjectParser(HttpClientUtils.JSON_FACTORY));
            });

    GenericUrl url = new GenericUrl(authOrTokenUri);

    Map<String, String> param = new HashMap<>();
    param.put("client_id", clientId);
    param.put("client_secret", clientSecret);
    param.put("grant_type", grant_type);

    if (grant_type.equalsIgnoreCase("authorization_code")) {
      param.put("code", tokenOrCode);
    } else {
      param.put("refresh_token", tokenOrCode);
    }

    HttpContent content = new UrlEncodedContent(param);

    HttpRequest request = requestFactory.buildPostRequest(url, content);

    Type type = new TypeToken<TokenResponse>() {}.getType();
    HttpResponse response = request.execute();
    TokenResponse tokenResponse = (TokenResponse) response.parseAs(type);

    return tokenResponse;
  }

  public static OAuth2Credential updateTokenIfNotValid(
      String clientId,
      String clientSecret,
      @NonNull OAuth2Credential credential,
      String tokenUrl,
      @NonNull Auth authProvider)
      throws IOException {
    if (!isValidToken(credential)) {
      TokenResponse response =
          getOrRefreshAccessToken(
              clientId, clientSecret, "refresh_token", credential.getRefreshToken(), tokenUrl);
      credential.setRefreshToken(response.getRefreshToken());
      credential.setAccessToken(response.getAccessToken());
      credential.setExpiresInSeconds(
          System.currentTimeMillis() / 1000 + response.getExpiresInSeconds());
      return authProvider.updateOAuth2Credential(credential);
    }
    return credential;
  }

  public static boolean isValidToken(@NonNull OAuth2Credential credential) {
    Long currentTimeInSeconds = System.currentTimeMillis() / 1000;
    return credential.getExpiresInSeconds() - currentTimeInSeconds >= 90L ? true : false;
  }
}
