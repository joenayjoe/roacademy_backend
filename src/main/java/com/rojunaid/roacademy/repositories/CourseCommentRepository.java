package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.CourseComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

  @Query(
      value =
          "SELECT cmnt from CourseComment cmnt LEFT JOIN FETCH cmnt.commentedBy WHERE cmnt.course.id = ?1",
      countQuery = "SELECT COUNT(cmnt) from CourseComment cmnt WHERE cmnt.course.id= ?1")
  Page<CourseComment> findAllByCourseId(Long courseId, Pageable pageable);

  @Query(
      value =
          "SELECT reply from CourseComment reply LEFT JOIN FETCH reply.commentedBy WHERE reply.parent.id = ?1",
      countQuery = "SELECT COUNT(reply) FROM CourseComment reply WHERE reply.parent.id=?1")
  Page<CourseComment> findCourseReplies(Long commentId, Pageable pageable);
}
