package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

  Optional<Chapter> getChapterByIdAndCourse(Long chapterId, Long courseId);
}
