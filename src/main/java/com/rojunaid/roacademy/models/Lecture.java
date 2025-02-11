package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Lecture extends Auditable {

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "lecture_tag",
      joinColumns = @JoinColumn(name = "lecture_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  @EqualsAndHashCode.Exclude
  Set<Tag> tags = new HashSet<>();
  private String name;
  private String description;
  private int position;
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", updatable = false)
  @EqualsAndHashCode.Exclude
  private User createdBy;

  @JsonBackReference
  @OneToMany(
      mappedBy = "lecture",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  private List<LectureResource> lectureResources = new ArrayList<>();

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chapter_id", nullable = false)
  @EqualsAndHashCode.Exclude
  private Chapter chapter;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "lecture",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<LectureComment> comments = new HashSet<>();

  @PrePersist
  public void addCreatedBy() {
    CustomUserPrincipal principal =
        (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = principal.getUser();
    this.setCreatedBy(user);
  }

  public void addLectureResource(LectureResource resource) {
    this.getLectureResources().add(resource);
    resource.setLecture(this);
  }

  public void addComment(LectureComment comment) {
    this.getComments().add(comment);
    comment.setLecture(this);
  }
}
