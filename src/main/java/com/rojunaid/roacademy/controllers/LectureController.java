package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.LecturePositionUpdateRequest;
import com.rojunaid.roacademy.dto.LectureResponse;
import com.rojunaid.roacademy.services.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("/{lectureId}/upload_content")
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
}
