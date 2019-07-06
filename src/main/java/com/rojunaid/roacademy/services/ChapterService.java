package com.rojunaid.roacademy.services;


import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.models.Chapter;


public interface ChapterService {

  Iterable<Chapter> getAllChapterForCourse(Long courseId);
  Chapter getChapterById(Long courseId, Long chapterId);
  Chapter createChapter(ChapterDTO chapterDTO);
  Chapter updateChapter(Long chapterId, ChapterDTO chapterDTO);
  void deleteChapter(Long chapterId);
}
