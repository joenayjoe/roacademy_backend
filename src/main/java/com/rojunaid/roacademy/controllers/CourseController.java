package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<Iterable<CourseResponse>> getAllCourse() {
    Iterable<CourseResponse> courseResponses = courseService.getAllCourse();
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
      @PathVariable Long courseId, @Valid @RequestBody CourseRequest courseRequest) {
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
