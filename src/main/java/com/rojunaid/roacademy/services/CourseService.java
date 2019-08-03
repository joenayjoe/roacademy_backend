package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.models.Course;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CourseService {

  Iterable<Course> getAllCourse();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Course createCourse(CourseDTO courseDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Course updateCourse(Long courseId, CourseDTO courseDTO);

  Course findCourseById(Long courseId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCourseById(Long courseId);
}
