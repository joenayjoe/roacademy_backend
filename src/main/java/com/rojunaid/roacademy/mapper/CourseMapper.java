package com.rojunaid.roacademy.mapper;

import com.rojunaid.roacademy.dto.error.CourseDTO;
import com.rojunaid.roacademy.models.Course;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {

  CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

  CourseDTO CourseToCourseDTO(Course course);
  Course CourseDTOToCourse(CourseDTO courseDTO);
}
