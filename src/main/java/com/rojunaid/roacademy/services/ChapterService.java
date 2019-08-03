package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.models.Chapter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ChapterService {

  Iterable<Chapter> getAllChapterForCourse(Long courseId);

  Chapter getChapterById(Long courseId, Long chapterId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Chapter createChapter(ChapterDTO chapterDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Chapter updateChapter(Long chapterId, ChapterDTO chapterDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteChapter(Long chapterId);
}
