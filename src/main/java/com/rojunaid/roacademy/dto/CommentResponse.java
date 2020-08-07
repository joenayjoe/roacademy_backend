package com.rojunaid.roacademy.dto;

import lombok.Data;

@Data
public class CommentResponse extends AuditableDTO {
  Long id;
  private String commentBody;
  private PrimaryUser commentedBy;
  private Long parentId;
  private int numberOfReplies;
}
