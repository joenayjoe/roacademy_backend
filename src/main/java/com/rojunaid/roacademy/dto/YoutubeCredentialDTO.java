package com.rojunaid.roacademy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class YoutubeCredentialDTO {
  @NotBlank(message = "{NotBlank.OAuth2Credential.refreshToken}")
  private String refreshToken;

  @NotBlank(message = "{NotBlank.OAuth2Credential.accessToken}")
  private String accessToken;

  @NotNull(message = "{NotNull.OAuth2Credential.expiresInSeconds}")
  private Long expiresInSeconds;
}
