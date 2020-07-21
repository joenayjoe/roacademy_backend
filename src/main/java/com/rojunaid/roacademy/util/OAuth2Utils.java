package com.rojunaid.roacademy.util;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.reflect.TypeToken;
import com.rojunaid.roacademy.auth.oauth2.Auth;
import com.rojunaid.roacademy.auth.oauth2.OAuth2RequestParams;
import com.rojunaid.roacademy.auth.oauth2.TokenResponse;
import com.rojunaid.roacademy.models.OAuth2Credential;
import lombok.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OAuth2Utils {

  public static TokenResponse getOrRefreshAccessToken(OAuth2RequestParams params)
      throws IOException {

    HttpRequestFactory requestFactory =
        HttpClientUtils.HTTP_TRANSPORT.createRequestFactory(
            (HttpRequest request) -> {
              request.setParser(new JsonObjectParser(HttpClientUtils.JSON_FACTORY));
            });

    GenericUrl url = new GenericUrl(params.getAuthOrTokenUrl());

    Map<String, String> param = new HashMap<>();
    param.put("client_id", params.getClientId());
    param.put("client_secret", params.getClientSecret());
    param.put("grant_type", params.getGrantType());

    if (params.getGrantType().equalsIgnoreCase("authorization_code")) {
      param.put("code", params.getCodeOrToken());
    } else {
      param.put("refresh_token", params.getCodeOrToken());
    }

    if (params.getRedirectUrl() != null) {
      param.put("redirect_uri", params.getRedirectUrl());
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

      OAuth2RequestParams params = new OAuth2RequestParams();
      params.setClientId(clientId);
      params.setClientSecret(clientSecret);
      params.setAuthOrTokenUrl(tokenUrl);
      params.setGrantType("refresh_token");
      params.setCodeOrToken(credential.getRefreshToken());

      TokenResponse response = getOrRefreshAccessToken(params);
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
