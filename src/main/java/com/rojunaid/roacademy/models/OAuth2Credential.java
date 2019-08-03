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

  @NotBlank(message = "{NotBlank.field}")
  private String clientId;

  @NotBlank(message = "{NotBlank.field}")
  private String clientSecret;

  @NotBlank(message = "{NotBlank.field}")
  private String accessToken;

  @NotBlank(message = "{NotBlank.field}")
  private String refreshToken;

  @NotBlank(message = "{NotBlank.field}")
  private String scope;

  @NotBlank(message = "{NotBlank.field}")
  private String tokenType;

  @NotNull(message = "{NotNull.field}")
  private Long expiresInSeconds;
}
