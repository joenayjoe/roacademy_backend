package com.rojunaid.roacademy.auth.oauth2;

import com.rojunaid.roacademy.models.OAuth2Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuth2CredentialService {

  @Autowired private OAuth2CredentialRepository oAuth2CredentialRepository;

  public OAuth2Credential getCredential() {
    Iterable<OAuth2Credential> credentials = oAuth2CredentialRepository.findAll();
    if (!credentials.iterator().hasNext()) {
      return null;
    }
    return credentials.iterator().next();
  }

  public OAuth2Credential createOrUpdateCredential(OAuth2Credential credential) {
    return oAuth2CredentialRepository.save(credential);
  }
}
