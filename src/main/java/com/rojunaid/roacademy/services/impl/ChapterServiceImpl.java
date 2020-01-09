package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.ChapterRequest;
import com.rojunaid.roacademy.dto.ChapterResponse;
import com.rojunaid.roacademy.dto.PrimaryCourse;
import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.repositories.ChapterRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.services.ChapterService;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ChapterServiceImpl implements ChapterService {

  @Autowired ChapterRepository chapterRepository;

  @Autowired CourseRepository courseRepository;

  @Autowired TagService tagService;

  @Override
  public Iterable<ChapterResponse> getAllChapterForCourse(Long courseId) {
    Course course = this.getCourse(courseId);
    Set<Chapter> chapters = course.getChapters();

    List<ChapterResponse> chapterResponses = new ArrayList<>();
    for (Chapter chapter : chapters) {
      chapterResponses.add(this.chapterToChapterResponse(chapter));
    }
    return chapterResponses;
  }

  @Override
  public ChapterResponse getChapterById(Long courseId, Long chapterId) {
    Chapter chapter =
        chapterRepository
            .getChapterByIdAndCourse(chapterId, courseId)
            .orElseThrow(() -> this.chapterNotFoundException(chapterId));
    return this.chapterToChapterResponse(chapter);
  }

  @Override
  public ChapterResponse createChapter(ChapterRequest chapterRequest) {
    Course course = this.getCourse(chapterRequest.getCourseId());
    Chapter chapter = this.chapterDTOToChapter(chapterRequest);
    chapter.setCourse(course);
    chapter = chapterRepository.save(chapter);
    return this.chapterToChapterResponse(chapter);
  }

  @Override
  public ChapterResponse updateChapter(Long chapterId, ChapterRequest chapterRequest) {
    Course course = this.getCourse(chapterRequest.getCourseId());
    Chapter chapter = this.chapterDTOToChapter(chapterRequest);
    chapter.setCourse(course);
    chapter.setId(chapterId);
    chapter = chapterRepository.save(chapter);
    return this.chapterToChapterResponse(chapter);
  }

  @Override
  public void deleteChapter(Long chapterId) {
    Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
    if (chapter != null) {
      chapterRepository.deleteById(chapterId);
    } else {
      throw this.chapterNotFoundException(chapterId);
    }
  }

  @Override
  public ChapterResponse chapterToChapterResponse(Chapter chapter) {

    Course course = chapter.getCourse();
    Long chapterId = chapter.getId();

    ChapterResponse chapterResponse = new ChapterResponse();
    chapterResponse.setId(chapterId);
    chapterResponse.setName(chapter.getName());
    chapterResponse.setCreatedAt(chapter.getCreatedAt());
    chapterResponse.setUpdatedAt(chapter.getUpdatedAt());

    PrimaryCourse primaryCourse = new PrimaryCourse();
    primaryCourse.setId(course.getId());
    primaryCourse.setName(course.getName());
    chapterResponse.setPrimaryCourse(primaryCourse);

    List<TagResponse> tagResponses = new ArrayList<>();
    for (Tag tag : chapter.getTags()) {
      tagResponses.add(tagService.tagToTagResponse(tag));
    }
    chapterResponse.setTags(tagResponses);

    return chapterResponse;
  }

  // private methods

  private Course getCourse(Long courseId) {
    return courseRepository.findById(courseId).orElse(null);
  }

  // private methods

  private ResourceNotFoundException chapterNotFoundException(Long chapterId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Chapter.id.notfound", new Object[] {chapterId}));
  }

  private Chapter chapterDTOToChapter(ChapterRequest chapterRequest) {
    Chapter chapter = new Chapter();
    chapter.setName(chapterRequest.getName());
    Set<Tag> tags = tagService.findOrCreateByNames(chapterRequest.getTagNames());
    chapter.setTags(tags);
    return chapter;
  }
}
