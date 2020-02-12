package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.LecturePositionUpdateRequest;
import com.rojunaid.roacademy.models.LectureResource;
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
  public ResponseEntity<LectureResource> uploadLectureResource(
      @PathVariable Long lectureId, @Valid @RequestParam MultipartFile file) {
    LectureResource resource = lectureService.uploadResource(lectureId, file);
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
