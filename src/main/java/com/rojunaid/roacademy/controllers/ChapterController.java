package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.services.ChapterService;
import com.rojunaid.roacademy.services.LectureService;
import com.rojunaid.roacademy.services.TeachingResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/courses/{courseId}/chapters")
public class ChapterController {

  @Autowired ChapterService chapterService;
  @Autowired TeachingResourceService teachingResourceService;
  @Autowired LectureService lectureService;

  // GET /api/courses/:courseId/chapters
  // Get all chapter for a course
  @GetMapping("")
  public ResponseEntity<Iterable<ChapterResponse>> getAllByCourseId(
      @PathVariable Long courseId, @RequestParam(defaultValue = "position_asc") String order) {

    Iterable<ChapterResponse> chapterResponses =
        chapterService.getAllChapterForCourse(courseId, order);
    return new ResponseEntity<>(chapterResponses, HttpStatus.OK);
  }

  // POST /api/courses/:courseId/chapters
  // Create a chapter for a course
  @PostMapping("")
  public ResponseEntity<ChapterResponse> createChapter(
      @Valid @RequestBody ChapterRequest chapterRequest) {

    ChapterResponse chapterResponse = chapterService.createChapter(chapterRequest);
    return new ResponseEntity<>(chapterResponse, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId/chapters/:chapterId
  // Update a chapter of a course
  @PutMapping("/{chapterId}")
  public ResponseEntity<ChapterResponse> updateChapter(
      @PathVariable Long chapterId, @Valid @RequestBody ChapterUpdateRequest chapterRequest) {

    ChapterResponse chapterResponse = chapterService.updateChapter(chapterId, chapterRequest);
    return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
  }

  // Update position of the chapters

  @PostMapping("/update_positions")
  public ResponseEntity<HttpStatus> updateChapterPosition(
      @PathVariable Long courseId,
      @Valid @RequestBody ChapterPositionUpdateRequest[] positionUpdateRequests) {
    chapterService.updateChapterPosition(courseId, positionUpdateRequests);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // GET /api/courses/:courseId/chapters/:chapterId
  // Get a chapter of a course
  @GetMapping("/{chapterId}")
  public ResponseEntity<ChapterResponse> getChapterById(
      @PathVariable Long courseId, @PathVariable Long chapterId) {
    ChapterResponse chapterResponse = chapterService.getChapterById(courseId, chapterId);
    return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId/chapters/:chapterId
  // Delete a chapter from a course
  @DeleteMapping("/{chapterId}")
  public ResponseEntity<HttpStatus> deleteChapterById(@PathVariable Long chapterId) {
    chapterService.deleteChapter(chapterId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  //  LECTURE API ENDPOINTS

  // Create lecture
  @PostMapping("/{chapterId}/lectures")
  public ResponseEntity<LectureResponse> createLecture(
      @PathVariable Long courseId,
      @PathVariable Long chapterId,
      @Valid @RequestBody LectureRequest lectureRequest) {
    LectureResponse lectureResponse = lectureService.createLecture(lectureRequest);
    return new ResponseEntity<>(lectureResponse, HttpStatus.CREATED);
  }

  // Update lecture

  @PutMapping("/{chapterId}/lectures/{lectureId}")
  public ResponseEntity<LectureResponse> updateLecture(
      @PathVariable Long chapterId,
      @PathVariable Long lectureId,
      @Valid @RequestBody LectureUpdateRequest lecture) {
    LectureResponse lectureResponse = lectureService.updateLecture(lectureId, lecture);
    return new ResponseEntity<>(lectureResponse, HttpStatus.OK);
  }

  // Delete lecture
  @DeleteMapping("/{chapterId}/lectures/{lectureId}")
  public ResponseEntity<HttpStatus> deleteLecture(
      @PathVariable Long chapterId, @PathVariable Long lectureId) {
    lectureService.deleteLecture(lectureId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
