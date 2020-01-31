package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.CourseUpdateRequest;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<CourseResponse> courseResponses = courseService.findAll(page, size, order);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // GET /api/courses
  // Get all courses for a category
  @GetMapping(value = "", params = "category_id")
  public ResponseEntity<Iterable<CourseResponse>> getAllCoursesByCategoryId(
      @RequestParam Long category_id,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) CourseStatusEnum[] status) {
    Iterable<CourseResponse> courseResponses =
        courseService.findCoursesByCategoryId(category_id, status, order);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // GET /api/courses
  // Get all courses for a grade
  @GetMapping(value = "", params = "grade_id")
  public ResponseEntity<Iterable<CourseResponse>> getAllCoursesByGradeId(
      @RequestParam Long grade_id,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) CourseStatusEnum[] status) {
    Iterable<CourseResponse> courseResponses =
        courseService.findCoursesByGradeId(grade_id, status, order);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // POST /api/courses
  // Create a course
  @PostMapping("")
  public ResponseEntity<CourseResponse> createCourse(
      @Valid @RequestBody CourseRequest courseRequest) {
    CourseResponse courseResponse = courseService.createCourse(courseRequest);
    return new ResponseEntity<>(courseResponse, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId
  // Update a course
  @PutMapping("/{courseId}")
  public ResponseEntity<CourseResponse> updateCourse(
      @PathVariable Long courseId, @Valid @RequestBody CourseUpdateRequest courseRequest) {
    CourseResponse courseResponse = courseService.updateCourse(courseId, courseRequest);
    return new ResponseEntity<>(courseResponse, HttpStatus.OK);
  }

  // GET /api/courses/:courseId
  // Get a course by ID
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
    CourseResponse courseResponse = courseService.findCourseById(courseId);
    return new ResponseEntity<>(courseResponse, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId
  // Delete a course
  @DeleteMapping("/{courseId}")
  public ResponseEntity<HttpStatus> deleteCourseById(@PathVariable Long courseId) {
    courseService.deleteCourseById(courseId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
