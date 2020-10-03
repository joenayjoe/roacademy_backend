package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.models.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {

  @PreAuthorize(
      "hasRole('ADMIN') or @permissionService.canManageChapter(#lectureRequest.chapterId)")
  LectureResponse createLecture(LectureRequest lectureRequest);

  @PreAuthorize(
      "hasRole('ADMIN') or @permissionService.canManageChapter(#lectureUpdateRequest.chapterId)")
  LectureResponse updateLecture(Long lectureId, LectureUpdateRequest lectureUpdateRequest);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageChapter(#positions[0].chapterId)")
  void updatePositions(LecturePositionUpdateRequest[] positions);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  LectureResponse uploadResource(Long lectureId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  void deleteLectureResource(Long lectureId, Long resourceId);

  @PreAuthorize("hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId)")
  void deleteLecture(Long lectureId);

  CommentResponse addComment(Long lectureId, CommentRequest commentRequest);

  CommentResponse addCommentReply(Long lectureId, Long commentId, CommentRequest commentRequest);

  Page<CommentResponse> getComments(Long lectureId, int page, int size, String order);

  Page<CommentResponse> getCommentReplies(
      Long lectureId, Long commentId, int page, int size, String order);

  @PreAuthorize(
      "hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId) or @permissionService.canManageLectureComment(#commentUpdateRequest.id)")
  CommentResponse updateComment(
      Long lectureId, Long commentId, CommentUpdateRequest commentUpdateRequest);

  @PreAuthorize(
      "hasRole('ADMIN') or @permissionService.canManageLecture(#lectureId) or @permissionService.canManageLectureComment(#commentId)")
  void deleteComment(Long lectureId, Long commentId);

  LectureResponse lectureToLectureResponse(Lecture lecture);
}
