package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.models.Course;

public interface CourseService {

  Iterable<Course> getAllCourse();

  Course createCourse(CourseDTO courseDTO);

  Course updateCourse(Long courseId, CourseDTO courseDTO);

  Course findCourseById(Long courseId);

  void deleteCourseById(Long courseId);
}
