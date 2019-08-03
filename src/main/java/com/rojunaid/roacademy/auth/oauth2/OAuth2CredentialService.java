package com.rojunaid.roacademy.auth.oauth2;

import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.OAuth2Credential;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuth2CredentialService {

  @Autowired private OAuth2CredentialRepository oAuth2CredentialRepository;

  public OAuth2Credential getYoutubeCredential() {
    Iterable<OAuth2Credential> credentials = oAuth2CredentialRepository.findAll();
    if (!credentials.iterator().hasNext()) {
      throw new ResourceNotFoundException(Translator.toLocale("Credential.notfound"));
    }
    return credentials.iterator().next();
  }

  public OAuth2Credential storeYoutubeCredential(OAuth2Credential credential) {
    return oAuth2CredentialRepository.save(credential);
  }
}
