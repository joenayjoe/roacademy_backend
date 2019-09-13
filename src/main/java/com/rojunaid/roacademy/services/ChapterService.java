package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.dto.ChapterResponse;
import com.rojunaid.roacademy.models.Chapter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ChapterService {

  Iterable<ChapterResponse> getAllChapterForCourse(Long courseId);

  ChapterResponse getChapterById(Long courseId, Long chapterId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  ChapterResponse createChapter(ChapterDTO chapterDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  ChapterResponse updateChapter(Long chapterId, ChapterDTO chapterDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteChapter(Long chapterId);

  ChapterResponse chapterToChapterResponse(Chapter chapter);
}
