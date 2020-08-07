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
public class CourseComment extends Auditable {

  @NotBlank(message = "{NotBlank.CourseComment.commentBody}")
  private String commentBody;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  @EqualsAndHashCode.Exclude
  private Course course;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "commented_by", updatable = false)
  @EqualsAndHashCode.Exclude
  private User commentedBy;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<CourseComment> replies = new HashSet<>();

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", updatable = false)
  @EqualsAndHashCode.Exclude
  private CourseComment parent;

  public void addCommentedBy(User user) {
    this.setCommentedBy(user);
    user.getCourseComments().add(this);
  }
}
