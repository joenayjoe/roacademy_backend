package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.LectureComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureCommentRepository extends JpaRepository<LectureComment, Long> {
  @Query(
      value =
          "SELECT cmnt from LectureComment cmnt LEFT JOIN FETCH cmnt.commentedBy LEFT JOIN FETCH cmnt.replies WHERE cmnt.lecture.id = ?1",
      countQuery = "SELECT COUNT(cmnt) from LectureComment cmnt WHERE cmnt.lecture.id= ?1")
  Page<LectureComment> findAllByLectureId(Long lectureId, Pageable pageable);

  @Query(
      value =
          "SELECT reply from LectureComment reply LEFT JOIN FETCH reply.commentedBy WHERE reply.parent.id = ?1",
      countQuery = "SELECT COUNT(reply) from LectureComment reply WHERE reply.parent.id=?1")
  Page<LectureComment> findCommentReplies(Long commentId, Pageable pageable);
}
