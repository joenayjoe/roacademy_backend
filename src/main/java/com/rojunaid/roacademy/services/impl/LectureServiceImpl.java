package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.LectureRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.dto.PrimaryChapter;
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

import javax.persistence.OneToOne;
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

  @OneToOne
  public LectureResponse lectureToLectureResponse(Lecture lecture) {
    LectureResponse response = new LectureResponse();
    response.setName(lecture.getName());
    response.setDescription(lecture.getDescription());

    response.setLectureResource(lecture.getLectureResource());
    response.setTags(lecture.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
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

  private Lecture lectureRequestToLecture(LectureRequest lectureRequest) {
    Lecture lecture = new Lecture();
    lecture.setName(lectureRequest.getName());
    lecture.setDescription(lectureRequest.getDescription());
    lecture.setChapter(getChapter(lectureRequest.getChapterId()));

    Set<Tag> tags = tagService.findOrCreateByNames(lectureRequest.getTags());
    lecture.setTags(tags);

    return lecture;
  }
}
