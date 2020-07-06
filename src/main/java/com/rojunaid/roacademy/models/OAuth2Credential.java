package com.rojunaid.roacademy.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "oauth2_credential")
@Data
public class OAuth2Credential extends Auditable {

  @NotBlank(message = "{NotBlank.OAuth2Credential.accessToken}")
  private String accessToken;

  @NotBlank(message = "{NotBlank.OAuth2Credential.refreshToken}")
  private String refreshToken;

  @NotNull(message = "{NotNull.OAuth2Credential.expiresInSeconds}")
  private Long expiresInSeconds;

  @NotBlank(message = "{NotNull.OAuth2Credential.provider}")
  private String provider;
}
