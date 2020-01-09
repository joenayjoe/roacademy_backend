package com.rojunaid.roacademy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "oauth2_credential")
@Getter
@Setter
public class OAuth2Credential extends Auditable {

  @NotBlank(message = "{NotBlank.OAuth2Credential.clientId}")
  private String clientId;

  @NotBlank(message = "{NotBlank.OAuth2Credential.clientSecret}")
  private String clientSecret;

  @NotBlank(message = "{NotBlank.OAuth2Credential.accessToken}")
  private String accessToken;

  @NotBlank(message = "{NotBlank.OAuth2Credential.refreshToken}")
  private String refreshToken;

  @NotBlank(message = "{NotBlank.OAuth2Credential.scope}")
  private String scope;

  @NotBlank(message = "{NotBlank.OAuth2Credential.tokenType}")
  private String tokenType;

  @NotNull(message = "{NotNull.OAuth2Credential.expiresInSeconds}")
  private Long expiresInSeconds;
}
