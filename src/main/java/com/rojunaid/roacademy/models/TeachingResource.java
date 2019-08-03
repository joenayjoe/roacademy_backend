package com.rojunaid.roacademy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TeachingResource extends Auditable {

  @NotBlank(message = "{NotBlank.field}")
  private String title;

  @NotBlank(message = "{NotBlank.field}")
  private String description;

  @NotBlank(message = "{NotBlank.field}")
  private String fileName;

  private String contentType;

  private Long fileSize;

  private String fileUrl;

  private String hostingType; // local or youtube

  private String privacyStatus;

  @NotNull(message = "{NotNull.field}")
  private Long resourceOwnerId;

  // resourceOwnerType can be chapter, course , or other entity name
  // it will be used for polymorphic query
  @NotBlank(message = "{NotBlank.field}")
  private String resourceOwnerType;

  @ManyToOne User user;

  @ManyToMany
  @JoinTable(
      name = "teaching_resource_tag",
      joinColumns = @JoinColumn(name = "teaching_resource_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  Set<Tag> tags = new HashSet<>();
}
