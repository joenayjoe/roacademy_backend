package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.models.Grade;

public interface GradeService {

  Iterable<Grade> getAllGrade();

  Grade createGrade(Long categoryId, GradeDTO gradeDTO);

  Grade updateGrade(Long categoryId, Long gradeId, GradeDTO gradeDTO);

  Grade findGradeById(Long gradeId);

  Iterable<Grade> findGradesByCategoryId(Long categoryId);

  void deleteGradeById(Long gradeId);
}
