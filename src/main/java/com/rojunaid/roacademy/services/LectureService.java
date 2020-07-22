package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.LecturePositionUpdateRequest;
import com.rojunaid.roacademy.dto.LectureRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.dto.LectureUpdateRequest;
import com.rojunaid.roacademy.models.Lecture;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageChapter(#lectureRequest.chapterId)")
  LectureResponse createLecture(LectureRequest lectureRequest);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageChapter(#lectureUpdateRequest.chapterId)")
  LectureResponse updateLecture(Long lectureId, LectureUpdateRequest lectureUpdateRequest);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageChapter(#positions[0].chapterId)")
  void updatePositions(LecturePositionUpdateRequest[] positions);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  LectureResponse uploadResource(Long lectureId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  void deleteLectureResource(Long lectureId, Long resourceId);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  void deleteLecture(Long lectureId);

  LectureResponse lectureToLectureResponse(Lecture lecture);
}
