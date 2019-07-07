package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.models.Course;
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
  public ResponseEntity<Iterable<Course>> getAllCourse() {
    Iterable<Course> courses = courseService.getAllCourse();
    return new ResponseEntity<>(courses, HttpStatus.OK);
  }

  // POST /api/courses
  // Create a course
  @PostMapping("")
  public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
    Course course = courseService.createCourse(courseDTO);
    return new ResponseEntity<>(course, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId
  // Update a course
  @PutMapping("/{courseId}")
  public ResponseEntity<Course> updateCourse(
      @PathVariable Long courseId, @Valid @RequestBody CourseDTO courseDTO) {
    Course course = courseService.updateCourse(courseId, courseDTO);
    return new ResponseEntity<>(course, HttpStatus.OK);
  }

  // GET /api/courses/:courseId
  // Get a course by ID
  @GetMapping("/{courseId}")
  public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
    Course course = courseService.findCourseById(courseId);
    return new ResponseEntity<>(course, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId
  // Delete a course
  @DeleteMapping("/{courseId}")
  public ResponseEntity<HttpStatus> deleteCourseById(@PathVariable Long courseId) {
    courseService.deleteCourseById(courseId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
