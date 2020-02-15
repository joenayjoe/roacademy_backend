package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.auth.oauth2.OAuth2CredentialService;
import com.rojunaid.roacademy.dto.YoutubeCredentialDTO;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.OAuth2Credential;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2CredentialController {

  @Autowired private OAuth2CredentialService oAuth2CredentialService;

  @GetMapping()
  public ResponseEntity<YoutubeCredentialDTO> getCredential() {
    OAuth2Credential credential = oAuth2CredentialService.getCredential();
    YoutubeCredentialDTO response = oauth2CredentialToDTO(credential);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<YoutubeCredentialDTO> updateCredential(
      @Valid @RequestBody YoutubeCredentialDTO request) {

    OAuth2Credential credential = oAuth2CredentialService.getCredential();
    if (credential == null) {
      throw new ResourceNotFoundException(Translator.toLocale("${Credential.notfound}"));
    }
    credential.setRefreshToken(request.getRefreshToken());
    credential.setAccessToken(request.getAccessToken());
    Long currentTimeInSeconds = System.currentTimeMillis() / 1000;
    credential.setExpiresInSeconds(currentTimeInSeconds+request.getExpiresInSeconds());

    credential = oAuth2CredentialService.createOrUpdateCredential(credential);

    YoutubeCredentialDTO response = oauth2CredentialToDTO(credential);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private YoutubeCredentialDTO oauth2CredentialToDTO(OAuth2Credential credential) {
    if (credential == null) {
      return null;
    }
    YoutubeCredentialDTO response = new YoutubeCredentialDTO();
    response.setAccessToken(credential.getAccessToken());
    response.setRefreshToken(credential.getRefreshToken());
    response.setExpiresInSeconds(credential.getExpiresInSeconds());
    return response;
  }
}
