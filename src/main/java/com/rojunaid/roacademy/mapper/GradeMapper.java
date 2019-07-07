package com.rojunaid.roacademy.mapper;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.models.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GradeMapper {
  GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

  Grade gradeDTOToGrade(GradeDTO gradeDTO);

  GradeDTO gradeToGradeDTO(Grade grade);
}
