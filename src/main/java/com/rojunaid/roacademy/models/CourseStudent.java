package com.rojunaid.roacademy.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseStudent extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private User student;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CourseStudent that = (CourseStudent) o;

    return Objects.equals(this.getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }
}
