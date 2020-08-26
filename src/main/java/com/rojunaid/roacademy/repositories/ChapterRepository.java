package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Chapter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  @Query(
      "SELECT chptr from Chapter chptr left join fetch chptr.course left join fetch chptr.lectures lctrs left join fetch lctrs.lectureResources left join fetch lctrs.tags where chptr.id = ?1 and chptr.course.id=?2")
  Optional<Chapter> findByChapterIdAndCourseId(Long chapterId, Long courseId);

  @Query(
      "SELECT DISTINCT chptr from Chapter chptr LEFT JOIN FETCH chptr.course LEFT JOIN FETCH chptr.lectures lcts LEFT JOIN FETCH lcts.tags LEFT JOIN FETCH lcts.lectureResources WHERE chptr.course.id=?1")
  Iterable<Chapter> findByCourseId(Long courseId, Sort sort);
}
