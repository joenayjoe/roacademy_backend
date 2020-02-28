package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Lecture;
import com.rojunaid.roacademy.repositories.ChapterRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.services.ChapterService;
import com.rojunaid.roacademy.services.LectureService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

  @Autowired ChapterRepository chapterRepository;

  @Autowired CourseRepository courseRepository;

  @Autowired LectureService lectureService;

  @Override
  public Iterable<ChapterResponse> getAllChapterForCourse(Long courseId, String order) {
    Iterable<Chapter> chapters =
        chapterRepository.findByCourseId(courseId, SortingUtils.SortBy(order));

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
            .findByChapterIdAndCourseId(chapterId, courseId)
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
  public ChapterResponse updateChapter(Long chapterId, ChapterUpdateRequest chapterRequest) {
    Chapter chapter = this.getChapter(chapterId);
    chapter.setName(chapterRequest.getName());
    chapter = chapterRepository.save(chapter);
    return this.chapterToChapterResponse(chapter);
  }

  @Override
  public void updateChapterPosition(
      Long courseId, ChapterPositionUpdateRequest[] positionUpdateRequests) {
    for (ChapterPositionUpdateRequest request : positionUpdateRequests) {
      Long chapterId = request.getChapterId();
      int position = request.getPosition();
      Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
      if (chapter != null) {
        chapter.setPosition(position);
        chapterRepository.save(chapter);
      }
    }
  }

  @Override
  public void deleteChapter(Long chapterId) {
    Chapter chapter = this.getChapter(chapterId);
    chapterRepository.delete(chapter);
  }

  @Override
  public ChapterResponse chapterToChapterResponse(Chapter chapter) {

    Course course = chapter.getCourse();

    ChapterResponse chapterResponse = new ChapterResponse();
    chapterResponse.setId(chapter.getId());
    chapterResponse.setName(chapter.getName());
    chapterResponse.setPosition(chapter.getPosition());
    chapterResponse.setCreatedAt(chapter.getCreatedAt());
    chapterResponse.setUpdatedAt(chapter.getUpdatedAt());

    PrimaryCourse primaryCourse = new PrimaryCourse();
    primaryCourse.setId(course.getId());
    primaryCourse.setName(course.getName());
    chapterResponse.setPrimaryCourse(primaryCourse);

    List<Lecture> lectures = new ArrayList<>(chapter.getLectures());
    lectures.sort(Comparator.comparingInt(Lecture::getPosition));
    List<LectureResponse> lectureResponses = new ArrayList<>();
    for (Lecture lecture : lectures) {
      lectureResponses.add(lectureService.lectureToLectureResponse(lecture));
    }
    chapterResponse.setLectures(lectureResponses);
    return chapterResponse;
  }

  // private methods

  private Course getCourse(Long courseId) {
    return courseRepository
        .findById(courseId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Course.id.notfound", new Object[] {courseId})));
  }

  private Chapter getChapter(Long chapterId) {
    return chapterRepository
        .findById(chapterId)
        .orElseThrow(() -> chapterNotFoundException(chapterId));
  }

  // private methods

  private ResourceNotFoundException chapterNotFoundException(Long chapterId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Chapter.id.notfound", new Object[] {chapterId}));
  }

  private Chapter chapterDTOToChapter(ChapterRequest chapterRequest) {
    Chapter chapter = new Chapter();
    chapter.setName(chapterRequest.getName());
    chapter.setPosition(chapterRequest.getPosition());
    return chapter;
  }
}
