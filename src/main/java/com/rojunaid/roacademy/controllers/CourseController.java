package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.error.CourseDTO;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.mapper.CourseMapper;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired CourseService courseService;
  @Autowired GradeService gradeService;

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
    Grade grade =
            gradeService
                    .findGradeById(courseDTO.getGradeId())
                    .orElseThrow(()
                            -> new ResourceNotFoundException("Grade with ID "+ courseDTO.getGradeId() +" not found"));
    Course course = CourseMapper.INSTANCE.CourseDTOToCourse(courseDTO);
    course.setGrade(grade);
    course = courseService.createCourse(course);
    return new ResponseEntity<>(course, HttpStatus.CREATED);
  }

  // PUT /api/courses/:courseId
  // Update a course
  @PutMapping("/{courseId}")
  public ResponseEntity<Course> updateCourse(
      @PathVariable Long courseId, @Valid @RequestBody Course course) {
    if (courseService.isCourseExist(courseId)) {
      Course course1 = courseService.updateCourse(course);
      return new ResponseEntity<>(course, HttpStatus.OK);
    }

    throw new ResourceNotFoundException("Course with id " + courseId + " does not exist");
  }

  // GET /api/courses/:courseId
  // Get a course by ID
  @GetMapping("/{courseId}")
  public ResponseEntity<Course> findCourseById(@PathVariable Long courseId) {
    Course course =
        courseService
            .findCourseById(courseId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Course with id " + courseId + " does not exist"));

    return new ResponseEntity<>(course, HttpStatus.OK);
  }

  // DELETE /api/courses/:courseId
  // Delete a course
  @DeleteMapping("/{courseId}")
  public ResponseEntity<HttpStatus> deleteCourseById(@PathVariable Long courseId) {
    if (courseService.isCourseExist(courseId)) {
      courseService.deleteCourseById(courseId);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    throw new ResourceNotFoundException("Course with id " + courseId + " does not exist");
  }
}
