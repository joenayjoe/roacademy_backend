package com.rojunaid.roacademy.dto.mapper;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.models.Grade;

public class GradeMapper {

  public static Grade gradeDTOToGrade(GradeDTO gradeDTO) {
    Grade grade = new Grade();
    grade.setName(gradeDTO.getName());
    return grade;
  }
}
