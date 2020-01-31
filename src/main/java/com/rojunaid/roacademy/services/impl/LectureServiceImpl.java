package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.LecturePositionUpdateRequest;
import com.rojunaid.roacademy.dto.LectureRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.dto.LectureUpdateRequest;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Lecture;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.repositories.ChapterRepository;
import com.rojunaid.roacademy.repositories.LectureRepository;
import com.rojunaid.roacademy.services.LectureService;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

  @Autowired private LectureRepository lectureRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private TagService tagService;

  @Override
  public LectureResponse createLecture(LectureRequest lectureRequest) {
    Lecture lecture = this.lectureRequestToLecture(lectureRequest);
    lecture = lectureRepository.save(lecture);
    return lectureToLectureResponse(lecture);
  }

  @Override
  public LectureResponse updateLecture(Long lectureId, LectureUpdateRequest request) {
    Lecture lecture = getLecture(lectureId);
    lecture.setName(request.getName());
    lecture.setDescription(request.getDescription());
    lecture.setPosition(request.getPosition());
    lecture.setChapter(this.getChapter(request.getChapterId()));

    Set<Tag> tags = tagService.findOrCreateByNames(request.getTags());
    lecture.setTags(tags);
    return lectureToLectureResponse(lectureRepository.save(lecture));
  }

  @Override
  public void updatePositions(LecturePositionUpdateRequest[] positions) {

    boolean isFailed = false;
    for (LecturePositionUpdateRequest request : positions) {
      Lecture lecture = lectureRepository.findById(request.getLectureId()).orElse(null);
      Chapter chapter = chapterRepository.findById(request.getChapterId()).orElse(null);
      if (lecture != null && chapter != null) {
        lecture.setChapter(chapter);
        lecture.setPosition(request.getPosition());
        lectureRepository.save(lecture);
      } else {
        isFailed = true;
      }
    }

    if (isFailed) {
      throw new BadRequestException(Translator.toLocale("${BadRequest}"));
    }
  }

  @Override
  public void deleteLecture(Long lectureId) {
    Lecture lecture = getLecture(lectureId);
    lectureRepository.delete(lecture);
  }

  @Override
  public LectureResponse lectureToLectureResponse(Lecture lecture) {
    LectureResponse response = new LectureResponse();
    response.setId(lecture.getId());
    response.setName(lecture.getName());
    response.setDescription(lecture.getDescription());
    response.setPosition(lecture.getPosition());
    response.setLectureResource(lecture.getLectureResource());
    response.setTags(
        lecture.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
    response.setCreatedAt(lecture.getCreatedAt());
    response.setUpdatedAt(lecture.getUpdatedAt());

    return response;
  }

  private Chapter getChapter(Long chapterId) {
    return chapterRepository
        .findById(chapterId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("${Chapter.id.notfound}", new Object[] {chapterId})));
  }

  private Lecture getLecture(Long lectureId) {
    return lectureRepository
        .findById(lectureId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("${Lecture.id.notfound}", new Object[] {lectureId})));
  }

  private Lecture lectureRequestToLecture(LectureRequest lectureRequest) {
    Lecture lecture = new Lecture();
    lecture.setName(lectureRequest.getName());
    lecture.setDescription(lectureRequest.getDescription());
    lecture.setPosition(lectureRequest.getPosition());
    lecture.setChapter(getChapter(lectureRequest.getChapterId()));

    Set<Tag> tags = tagService.findOrCreateByNames(lectureRequest.getTags());
    lecture.setTags(tags);

    return lecture;
  }
}
