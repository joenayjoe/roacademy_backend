package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.CourseStatusUpdateRequest;
import com.rojunaid.roacademy.dto.CourseUpdateRequest;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CourseService {

  Page<CourseResponse> findAll(int page, int size, String order, List<CourseStatusEnum> status);

  Page<CourseResponse> findCoursesByCategoryId(
      Long category_id, int page, int size, List<CourseStatusEnum> status, String order);

  Page<CourseResponse> findCoursesByGradeId(
      Long grade_id, int page, int size, List<CourseStatusEnum> status, String order);

  Iterable<CourseResponse> findCoursesByGradeId(
      Long gradeId, List<CourseStatusEnum> status, String order);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse createCourse(CourseRequest courseRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse updateCourse(Long courseId, CourseUpdateRequest courseRequest);

  CourseResponse findCourseById(Long courseId, List<CourseStatusEnum> status);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void updateStatus(Long courseId, CourseStatusUpdateRequest request);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCourseById(Long courseId);

  Iterable<CourseResponse> search(String query);

  // DTO Mapper
  CourseResponse courseToCourseResponse(Course course);
}
