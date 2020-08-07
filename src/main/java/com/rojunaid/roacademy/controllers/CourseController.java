package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired CourseService courseService;

  // GET /api/courses
  // Get all courses
  @GetMapping("")
  public ResponseEntity<Iterable<CourseResponse>> getAllCourses(
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status) {
    Page<CourseResponse> courseResponses = courseService.findAll(page, size, order, status);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // GET /api/courses
  // Get all courses for a category
  @GetMapping(value = "", params = "category_id")
  public ResponseEntity<Page<CourseResponse>> getAllCoursesByCategoryId(
      @RequestParam Long category_id,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status) {
    Page<CourseResponse> courseResponses =
        courseService.findCoursesByCategoryId(category_id, page, size, status, order);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // GET /api/courses
  // Get all courses for a grade
  @GetMapping(value = "", params = "grade_id")
  public ResponseEntity<?> getAllCoursesByGradeId(
      @RequestParam Long grade_id,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status,
      @RequestParam(defaultValue = "true") boolean pagination) {
    if (pagination) {
      Page<CourseResponse> courseResponses =
          courseService.findCoursesByGradeId(grade_id, page, size, status, order);
      return new ResponseEntity<>(courseResponses, HttpStatus.OK);
    } else {
      Iterable<CourseResponse> courseResponses =
          courseService.findCoursesByGradeId(grade_id, status, order);
      return new ResponseEntity<>(courseResponses, HttpStatus.OK);
    }
  }

  // POST /api/courses
  // Create a course
  @PostMapping("")
  public ResponseEntity<CourseResponse> createCourse(
      @Valid @RequestPart("courseData") CourseRequest courseData,
      @RequestPart(required = false) MultipartFile file) {
    CourseResponse courseResponse = courseService.createCourse(courseData, file);
    return new ResponseEntity<>(courseResponse, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId
  // Update a course
  @PutMapping("/{courseId}")
  public ResponseEntity<?> updateCourse(
      @PathVariable Long courseId,
      @Valid @RequestPart("courseData") CourseUpdateRequest courseData,
      @RequestPart(required = false) MultipartFile file) {
    CourseResponse courseResponse = courseService.updateCourse(courseId, courseData, file);
    return new ResponseEntity<>(courseResponse, HttpStatus.OK);
  }

  // GET /api/courses/:courseId
  // Get a course by ID
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseResponse> getCourseById(
      @PathVariable Long courseId,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status) {
    CourseResponse courseResponse = courseService.findCourseById(courseId, status);
    return new ResponseEntity<>(courseResponse, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<CourseResponse>> searchCourseByKeyword(
      @RequestParam String kw,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status) {
    Page<CourseResponse> courseResponses =
        courseService.searchCoursesByKeyword(kw, page, size, order, status);

    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // Update course status

  @PostMapping("/{courseId}")
  public ResponseEntity<HttpStatus> updateCourseStatus(
      @PathVariable Long courseId, @Valid @RequestBody CourseStatusUpdateRequest request) {
    courseService.updateStatus(courseId, request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId
  // Delete a course
  @DeleteMapping("/{courseId}")
  public ResponseEntity<HttpStatus> deleteCourseById(@PathVariable Long courseId) {
    courseService.deleteCourseById(courseId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // POST /api/courses/:courseId/comments
  // Create a comment
  @PostMapping("/{courseId}/comments")
  public ResponseEntity<CommentResponse> addComment(
      @PathVariable Long courseId, @Valid @RequestBody CommentRequest comment) {
    CommentResponse courseComment = courseService.addComment(courseId, comment);
    return new ResponseEntity<>(courseComment, HttpStatus.CREATED);
  }

  // POST /api/courses/:courseId/comments/:commentId/replies
  // Add a reply to comment
  @PostMapping("/{courseId}/comments/{commentId}/replies")
  public ResponseEntity<CommentResponse> addCommentReply(
      @PathVariable Long courseId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentRequest commentRequest) {
    CommentResponse commentResponse =
        courseService.addCommentReply(courseId, commentId, commentRequest);
    return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
  }

  // GET /api/courses/:courseId/comments?page=PAGE&SIZE=SIZE
  // Get comments for course
  @GetMapping("/{courseId}/comments")
  public ResponseEntity<Page<CommentResponse>> getComments(
      @PathVariable Long courseId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {

    Page<CommentResponse> courseComments =
        courseService.getCourseComments(courseId, page, size, order);
    return new ResponseEntity<>(courseComments, HttpStatus.OK);
  }

  @GetMapping("/{courseId}/comments/{commentId}/replies")
  public ResponseEntity<Page<CommentResponse>> getReplies(
      @PathVariable Long courseId,
      @PathVariable Long commentId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<CommentResponse> responses =
        courseService.getCommentReplies(courseId, commentId, page, size, order);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  // PUT /api/courses/:courseId/comments/:commentId
  // Update a comment
  @PutMapping("/{courseId}/comments/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(
      @PathVariable Long courseId,
      @PathVariable Long commentId,
      @Valid @RequestBody CommentUpdateRequest comment) {
    CommentResponse courseComment = courseService.updateComment(courseId, commentId, comment);
    return new ResponseEntity<>(courseComment, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId/comments/:commentId
  // Delete a comment
  @DeleteMapping("/{courseId}/comments/{commentId}")
  public ResponseEntity<HttpStatus> deleteComment(
      @PathVariable Long courseId, @PathVariable Long commentId) {
    courseService.deleteComment(courseId, commentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
