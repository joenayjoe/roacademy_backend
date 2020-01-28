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
  public ResponseEntity<Iterable<ChapterResponse>> getAllChapter(@PathVariable Long courseId) {

    Iterable<ChapterResponse> chapterResponses = chapterService.getAllChapterForCourse(courseId);
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

  // GET /api/courses/:courseId/chapters/:chapterId
  // Get a chapter of a course
  @GetMapping("/{chapterId}")
  public ResponseEntity<ChapterResponse> getChapterById(
      @PathVariable Long courseId, @PathVariable Long chapterId) {
    ChapterResponse chapterResponse = chapterService.getChapterById(courseId, chapterId);
    return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
  }

  @PostMapping("/{chapterId}/lectures")
  public ResponseEntity<LectureResponse> addLecture(
      @PathVariable Long courseId, @PathVariable Long chapterId, @Valid @RequestBody LectureRequest lectureRequest) {
    LectureResponse lectureResponse = lectureService.createLecture(lectureRequest);
    return new ResponseEntity<>(lectureResponse, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId/chapters/:chapterId
  // Delete a chapter from a course
  @DeleteMapping("/{chapterId}")
  public ResponseEntity<HttpStatus> deleteChapterById(@PathVariable Long chapterId) {
    chapterService.deleteChapter(chapterId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
