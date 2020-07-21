package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

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
  CourseResponse createCourse(CourseRequest courseData, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CourseResponse updateCourse(Long courseId, CourseUpdateRequest courseData, MultipartFile file);

  CourseResponse findCourseById(Long courseId, List<CourseStatusEnum> status);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void updateStatus(Long courseId, CourseStatusUpdateRequest request);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCourseById(Long courseId);

  Page<SearchResponse> getAutocompleteSuggestionsForCourse(
      String query, int page, int size, String order, List<CourseStatusEnum> status);

  Page<CourseResponse> searchCoursesByKeyword(
      String kw, int page, int size, String order, List<CourseStatusEnum> status);

  Page<CourseResponse> findCoursesByInstructor(
      Long id, int page, int size, List<CourseStatusEnum> statusEnums, String order);

  // DTO Mapper
  CourseResponse courseToCourseResponse(Course course);
}
