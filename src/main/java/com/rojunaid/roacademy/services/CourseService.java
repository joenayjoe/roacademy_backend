package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.models.Course;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CourseService {

  Iterable<CourseResponse> getAllCourse(String order);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse createCourse(CourseRequest courseRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse updateCourse(Long courseId, CourseRequest courseRequest);

  CourseResponse findCourseById(Long courseId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCourseById(Long courseId);


  Iterable<CourseResponse> search(String query);

  // DTO Mapper
  CourseResponse courseToCourseResponse(Course course);
}
