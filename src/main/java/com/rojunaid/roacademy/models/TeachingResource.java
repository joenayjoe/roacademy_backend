package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TeachingResource extends Auditable {

  @JsonIgnore @ManyToOne User user;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "teaching_resource_tag",
      joinColumns = @JoinColumn(name = "teaching_resource_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  Set<Tag> tags = new HashSet<>();

  private String title;

  private String description;

  private String fileName;
  private String contentType;
  private Long fileSize;
  private String fileUrl;
  private String hostingType; // local or youtube
  private String privacyStatus;

  private Long resourceOwnerId;
  // resourceOwnerType can be chapter, course , or other entity name
  // it will be used for polymorphic query

  private String resourceOwnerType;
}
