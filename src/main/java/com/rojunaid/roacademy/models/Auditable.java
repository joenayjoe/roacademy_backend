package com.rojunaid.roacademy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM YYYY 'at' hh:mm:ss a")
  private Date createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM YYYY 'at' hh:mm:ss a")
  private Date updatedAt;
}
