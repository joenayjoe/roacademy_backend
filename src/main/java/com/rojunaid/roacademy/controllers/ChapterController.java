package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.TeachingResource;
import com.rojunaid.roacademy.services.ChapterService;
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

  // GET /api/courses/:courseId/chapters
  // Get all chapter for a course
  @GetMapping("")
  public ResponseEntity<Iterable<Chapter>> getAllChapter(@PathVariable Long courseId) {

    Iterable<Chapter> chapters = chapterService.getAllChapterForCourse(courseId);
    return new ResponseEntity<>(chapters, HttpStatus.OK);
  }

  // POST /api/courses/:courseId/chapters
  // Create a chapter for a course
  @PostMapping("")
  public ResponseEntity<Chapter> createChapter(@Valid @RequestBody ChapterDTO chapterDTO) {

    Chapter chapter = chapterService.createChapter(chapterDTO);
    return new ResponseEntity<>(chapter, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId/chapters/:chapterId
  // Update a chapter of a course
  @PutMapping("/{chapterId}")
  public ResponseEntity<Chapter> updateChapter(
      @PathVariable Long chapterId, @Valid @RequestBody ChapterDTO chapterDTO) {

    Chapter chapter = chapterService.updateChapter(chapterId, chapterDTO);
    return new ResponseEntity<>(chapter, HttpStatus.OK);
  }

  // GET /api/courses/:courseId/chapters/:chapterId
  // Get a chapter of a course
  @GetMapping("/{chapterId}")
  public ResponseEntity<Chapter> getChapterById(
      @PathVariable Long courseId, @PathVariable Long chapterId) {
    Chapter chapter = chapterService.getChapterById(courseId, chapterId);
    return new ResponseEntity<>(chapter, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId/chapters/:chapterId
  // Delete a chapter from a course
  @DeleteMapping("/{chapterId}")
  public ResponseEntity<HttpStatus> deleteChapterById(
      @PathVariable Long courseId, @PathVariable Long chapterId) {
    chapterService.deleteChapter(chapterId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{chapterId}/resources")
  public ResponseEntity<Iterable<TeachingResource>> getTeachingResourcesByChapterId(
          @PathVariable Long chapterId) {
    Iterable<TeachingResource> teachingResources =
            teachingResourceService.getTeachingResourceByChapter(chapterId);
    return new ResponseEntity<>(teachingResources, HttpStatus.OK);
  }
}
