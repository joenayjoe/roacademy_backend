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

  @NotBlank(message = "{NotBlank.TeachingResource.title}")
  private String title;

  @NotBlank(message = "{NotBlank.TeachingResource.description}")
  private String description;

  private Set<String> tagNames = new HashSet<>();

  private String privacyStatus;

  @NotNull(message = "{NotNull.TeachingResource.resourceOwnerId}")
  private Long resourceOwnerId;

  @NotBlank(message = "{NotBlank.TeachingResource.resourceOwnerType}")
  private String resourceOwnerType;
}
