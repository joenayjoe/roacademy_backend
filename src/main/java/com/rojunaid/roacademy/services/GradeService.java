package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.dto.GradeUpdateRequest;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.security.access.prepost.PreAuthorize;

public interface GradeService {

  Iterable<Grade> getAllGrade();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  GradeResponse createGrade(Long categoryId, GradeRequest gradeRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  GradeResponse updateGrade(Long categoryId, Long gradeId, GradeUpdateRequest gradeUpdateRequest);

  GradeResponse findGradeById(Long gradeId);
  GradeResponse findGradeWithCoursesById(Long gradeId);

  Iterable<GradeResponse> findGradesByCategoryId(Long categoryId, String order);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteGradeById(Long gradeId);

  Iterable<CourseResponse> findCoursesByGradeId(Long gradeId);

  GradeResponse gradeToGradeResponse(Grade grade);
}
