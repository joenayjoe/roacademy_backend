package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.mapper.ChapterMapper;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.repositories.ChapterRepository;
import com.rojunaid.roacademy.services.ChapterService;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ChapterServiceImpl implements ChapterService {

  @Autowired ChapterRepository chapterRepository;

  @Autowired CourseService courseService;

  @Autowired TagService tagService;

  @Override
  public Iterable<Chapter> getAllChapterForCourse(Long courseId) {
    Course course = this.getCourse(courseId);
    return course.getChapters();
  }

  @Override
  public Chapter getChapterById(Long courseId, Long chapterId) {
    return chapterRepository
        .getChapterByIdAndCourse(chapterId, courseId)
        .orElseThrow(() -> this.chapterNotFoundException(chapterId));
  }

  @Override
  public Chapter createChapter(ChapterDTO chapterDTO) {
    Course course = this.getCourse(chapterDTO.getCourseId());
    Chapter chapter = ChapterMapper.INSTANCE.chapterDTOToChapter(chapterDTO);
    Set<Tag> tags = this.getOrCreateTags(chapterDTO.getTagNames());
    chapter.setTags(tags);
    chapter.setCourse(course);
    return chapterRepository.save(chapter);
  }

  @Override
  public Chapter updateChapter(Long chapterId, ChapterDTO chapterDTO) {
    Course course = this.getCourse(chapterDTO.getCourseId());
    Chapter chapter = ChapterMapper.INSTANCE.chapterDTOToChapter(chapterDTO);
    Set<Tag> tags = this.getOrCreateTags(chapterDTO.getTagNames());
    chapter.setTags(tags);
    chapter.setCourse(course);
    chapter.setId(chapterId);
    return chapterRepository.save(chapter);
  }

  @Override
  public void deleteChapter(Long chapterId) {
    if (chapterRepository.existsById(chapterId)) {
      chapterRepository.deleteById(chapterId);
    }
    throw this.chapterNotFoundException(chapterId);
  }

  // private methods

  private Course getCourse(Long courseId) {
    return courseService.findCourseById(courseId);
  }

  private Set<Tag> getOrCreateTags(Set<String> tag_names) {
    Set<Tag> tags = new HashSet<>();
    for (String name : tag_names) {
      tags.add(tagService.findOrCreateByName(name.trim()));
    }
    return tags;
  }

  // private methods

  private ResourceNotFoundException chapterNotFoundException(Long chapterId) {
    return new ResourceNotFoundException("Chapter with id " + chapterId + " not found");
  }
}
