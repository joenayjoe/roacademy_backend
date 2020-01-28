package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  @Query(
      "SELECT chptr from Chapter chptr left join fetch chptr.course left join fetch chptr.lectures lctrs left join fetch lctrs.lectureResource left join fetch lctrs.tags where chptr.id = ?1 and chptr.course.id=?2")
  Optional<Chapter> findByChapterIdAndCourseId(Long chapterId, Long courseId);
}
