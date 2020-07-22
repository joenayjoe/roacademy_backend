package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.GradeRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.dto.GradeUpdateRequest;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface GradeService {

  Page<GradeResponse> findAll(int page, int size, String order);

  @PreAuthorize("hasRole('ADMIN')")
  GradeResponse createGrade(GradeRequest gradeRequest);

  @PreAuthorize("hasRole('ADMIN')")
  GradeResponse updateGrade(Long gradeId, GradeUpdateRequest gradeUpdateRequest);

  GradeResponse findGradeById(Long gradeId);

  GradeResponse findGradeWithCoursesById(Long gradeId, List<CourseStatusEnum> status);

  Iterable<GradeResponse> findGradesByCategoryId(Long categoryId, String order);

  @PreAuthorize("hasRole('ADMIN')")
  void deleteGradeById(Long gradeId);

  GradeResponse gradeToGradeResponse(Grade grade);
}
