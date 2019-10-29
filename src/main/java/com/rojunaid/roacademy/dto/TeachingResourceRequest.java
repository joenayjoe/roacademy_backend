package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TeachingResourceRequest {

  @NotBlank(message = "{NotBlank.field}")
  private String title;

  @NotBlank(message = "{NotBlank.field}")
  private String description;

  private Set<String> tagNames = new HashSet<>();

  private String privacyStatus;

  @NotNull(message = "{NotNull.field}")
  private Long resourceOwnerId;

  @NotBlank(message = "{NotBlank.field}")
  private String resourceOwnerType;
}
