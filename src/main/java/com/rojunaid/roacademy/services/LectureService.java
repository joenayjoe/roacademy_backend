package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.LectureRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.models.Lecture;

public interface LectureService {

  LectureResponse createLecture(LectureRequest lectureRequest);
  LectureResponse lectureToLectureResponse(Lecture lecture);
}
