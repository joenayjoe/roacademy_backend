package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.security.access.prepost.PreAuthorize;

public interface GradeService {

  Iterable<Grade> getAllGrade();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  GradeResponse createGrade(Long categoryId, GradeRequest gradeRequest);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  GradeResponse updateGrade(Long categoryId, Long gradeId, GradeRequest gradeRequest);

  GradeResponse findGradeById(Long gradeId);

  Iterable<GradeResponse> findGradesByCategoryId(Long categoryId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteGradeById(Long gradeId);

  Iterable<CourseResponse> findCoursesByGradeId(Long gradeId);

  GradeResponse gradeToGradeResponse(Grade grade);
}
