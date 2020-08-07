package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.services.LectureService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {
  @Autowired private LectureService lectureService;

  @PostMapping("/update_positions")
  public ResponseEntity<HttpStatus> updateLecturePositions(
      @Valid @RequestBody LecturePositionUpdateRequest[] positions) {
    lectureService.updatePositions(positions);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{lectureId}/resources")
  public ResponseEntity<LectureResponse> uploadLectureResource(
      @PathVariable Long lectureId, @Valid @RequestParam MultipartFile file) {
    LectureResponse response = lectureService.uploadResource(lectureId, file);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/{lectureId}/resources/{resourceId}")
  public ResponseEntity<HttpStatus> deleteLectureResource(
      @PathVariable Long lectureId, @PathVariable Long resourceId) {
    lectureService.deleteLectureResource(lectureId, resourceId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{lectureId}/comments")
  public ResponseEntity<CommentResponse> addComment(
      @PathVariable Long lectureId, @Valid @RequestBody CommentRequest commentRequest) {
    CommentResponse comment = lectureService.addComment(lectureId, commentRequest);
    return new ResponseEntity<>(comment, HttpStatus.CREATED);
  }

  @PostMapping("/{lectureId}/comments/{commentId}/replies")
  public ResponseEntity<CommentResponse> addCommentReply(
      @PathVariable Long lectureId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentRequest commentRequest) {
    CommentResponse commentResponse =
        lectureService.addCommentReply(lectureId, commentId, commentRequest);
    return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
  }

  @GetMapping("/{lectureId}/comments/{commentId}/replies")
  public ResponseEntity<Page<CommentResponse>> getReplies(
      @PathVariable Long lectureId,
      @PathVariable Long commentId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<CommentResponse> responses =
        lectureService.getCommentReplies(lectureId, commentId, page, size, order);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping("/{lectureId}/comments")
  public ResponseEntity<Page<CommentResponse>> getComments(
      @PathVariable Long lectureId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<CommentResponse> comments = lectureService.getComments(lectureId, page, size, order);
    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @PutMapping("/{lectureId}/comments/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(
      @PathVariable Long lectureId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
    CommentResponse comment =
        lectureService.updateComment(lectureId, commentId, commentUpdateRequest);
    return new ResponseEntity<>(comment, HttpStatus.OK);
  }

  @DeleteMapping("/{lectureId}/comments/{commentId}")
  public ResponseEntity<HttpStatus> deleteComment(
      @PathVariable Long lectureId, @PathVariable Long commentId) {
    lectureService.deleteComment(lectureId, commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
