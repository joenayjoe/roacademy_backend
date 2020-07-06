package com.rojunaid.roacademy.auth.oauth2.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.youtube.YouTube;
import com.rojunaid.roacademy.auth.oauth2.Auth;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.models.AuthProvider;
import com.rojunaid.roacademy.models.OAuth2Credential;
import com.rojunaid.roacademy.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YoutubeApiConnectionProvider {
  private YouTube youtubeApiProvider;
  private GoogleCredential currentGoogleCredential;

  @Autowired private Auth authProvider;

  public YouTube getYoutubeApiProvider() {
    if (youtubeApiProvider == null) {
      youtubeApiProvider =
          new YouTube.Builder(
                  HttpClientUtils.HTTP_TRANSPORT,
                  HttpClientUtils.JSON_FACTORY,
                  getGoogleCredential())
              .setApplicationName("roacademy-youtube-video-upload")
              .build();
    }
    return youtubeApiProvider;
  }

  private Credential getGoogleCredential() {
    OAuth2Credential oAuth2Credential =
        authProvider
            .getOAuth2Credential(AuthProvider.youtube)
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Youtube API is not configured. Please contact the admin."));

    if (currentGoogleCredential == null) {
      currentGoogleCredential =
          new GoogleCredential.Builder()
              .setClientSecrets(
                  authProvider.getClientId(AuthProvider.youtube),
                  authProvider.getClientSecret(AuthProvider.youtube))
              .setTransport(HttpClientUtils.HTTP_TRANSPORT)
              .setJsonFactory(HttpClientUtils.JSON_FACTORY)
              .build();

      currentGoogleCredential.setAccessToken(oAuth2Credential.getAccessToken());
      currentGoogleCredential.setRefreshToken(oAuth2Credential.getRefreshToken());
      currentGoogleCredential.setExpiresInSeconds(oAuth2Credential.getExpiresInSeconds());
      currentGoogleCredential.setExpirationTimeMilliseconds(
          oAuth2Credential.getExpiresInSeconds() * 1000);

    } else if (!currentGoogleCredential.getRefreshToken().equals(oAuth2Credential.getRefreshToken())
        || !currentGoogleCredential.getAccessToken().equals(oAuth2Credential.getAccessToken())) {
      oAuth2Credential.setAccessToken(currentGoogleCredential.getAccessToken());
      oAuth2Credential.setRefreshToken(currentGoogleCredential.getRefreshToken());
      oAuth2Credential.setExpiresInSeconds(currentGoogleCredential.getExpiresInSeconds());
      authProvider.updateOAuth2Credential(oAuth2Credential);
    }
    return currentGoogleCredential;
  }
}
