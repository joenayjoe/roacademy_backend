package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.models.Course;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CourseService {

  Iterable<CourseResponse> getAllCourse();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse createCourse(CourseDTO courseDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse updateCourse(Long courseId, CourseDTO courseDTO);

  CourseResponse findCourseById(Long courseId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCourseById(Long courseId);

  CourseResponse courseToCourseResponse(Course course);
}
