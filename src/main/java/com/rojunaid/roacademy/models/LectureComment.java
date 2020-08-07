package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class LectureComment extends Auditable {

  @NotBlank(message = "{NotBlank.LectureComment.commentBody}")
  private String commentBody;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "commented_by", updatable = false)
  @EqualsAndHashCode.Exclude
  private User commentedBy;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<LectureComment> replies = new HashSet<>();

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", updatable = false)
  @EqualsAndHashCode.Exclude
  private LectureComment parent;

  public void addCommentedBy(User user) {
    this.setCommentedBy(user);
    user.getLectureComments().add(this);
  }
}
