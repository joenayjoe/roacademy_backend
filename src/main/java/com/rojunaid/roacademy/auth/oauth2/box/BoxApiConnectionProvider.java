package com.rojunaid.roacademy.auth.oauth2.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIConnectionListener;
import com.box.sdk.BoxAPIException;
import com.rojunaid.roacademy.auth.oauth2.Auth;
import com.rojunaid.roacademy.auth.oauth2.OAuth2CredentialService;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.models.AuthProvider;
import com.rojunaid.roacademy.models.OAuth2Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BoxApiConnectionProvider {
  private BoxAPIConnection apiConnection;
  private OAuth2Credential currentCredential;

  @Autowired private OAuth2CredentialService oAuth2CredentialService;
  @Autowired private Auth authProvider;

  public BoxAPIConnection getConnection() {
    OAuth2Credential credential =
        authProvider
            .getOAuth2Credential(AuthProvider.box)
            .orElseThrow(
                () ->
                    new BadRequestException("Box API is not configured. Please contact the admin"));

    String clientId = authProvider.getClientId(AuthProvider.box);
    String clientSecret = authProvider.getClientSecret(AuthProvider.box);

    if (!Objects.equals(currentCredential, credential) || apiConnection == null) {
      currentCredential = credential;
      apiConnection =
          new BoxAPIConnection(
              clientId, clientSecret, credential.getAccessToken(), credential.getRefreshToken());
      apiConnection.addListener(
          new BoxAPIConnectionListener() {

            @Override
            public void onRefresh(BoxAPIConnection connection) {
              credential.setAccessToken(apiConnection.getAccessToken());
              credential.setRefreshToken(apiConnection.getRefreshToken());
              Long currentTime = System.currentTimeMillis();
              credential.setExpiresInSeconds((currentTime + apiConnection.getExpires()) / 1000L);
              oAuth2CredentialService.createOrUpdateCredential(credential);
            }

            @Override
            public void onError(BoxAPIConnection connection, BoxAPIException e) {
              System.out.println(e.getLocalizedMessage());
            }
          });
      apiConnection.refresh();
    }
    return apiConnection;
  }
}
