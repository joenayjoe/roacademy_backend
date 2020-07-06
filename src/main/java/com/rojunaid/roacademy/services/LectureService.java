package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.LecturePositionUpdateRequest;
import com.rojunaid.roacademy.dto.LectureRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.dto.LectureUpdateRequest;
import com.rojunaid.roacademy.models.Lecture;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  LectureResponse createLecture(LectureRequest lectureRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  LectureResponse updateLecture(Long lectureId, LectureUpdateRequest lectureUpdateRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void updatePositions(LecturePositionUpdateRequest[] positions);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  LectureResponse uploadResource(Long lectureId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteLectureResource(Long lectureId, Long resourceId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteLecture(Long lectureId);

  LectureResponse lectureToLectureResponse(Lecture lecture);
}
