package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.security.access.prepost.PreAuthorize;

public interface GradeService {

  Iterable<Grade> getAllGrade();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Grade createGrade(Long categoryId, GradeDTO gradeDTO);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Grade updateGrade(Long categoryId, Long gradeId, GradeDTO gradeDTO);

  Grade findGradeById(Long gradeId);

  Iterable<Grade> findGradesByCategoryId(Long categoryId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteGradeById(Long gradeId);
}
