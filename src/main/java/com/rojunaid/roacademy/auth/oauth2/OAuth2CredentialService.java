package com.rojunaid.roacademy.auth.oauth2;

import com.rojunaid.roacademy.models.OAuth2Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuth2CredentialService {

  @Autowired private OAuth2CredentialRepository oAuth2CredentialRepository;

  public Optional<OAuth2Credential> getCredentialByProvider(String provider) {
    return oAuth2CredentialRepository.findFirstByProvider(provider);
  }

  public OAuth2Credential createOrUpdateCredential(OAuth2Credential credential) {
    return oAuth2CredentialRepository.save(credential);
  }
}
