package com.rojunaid.roacademy.dto.mapper;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.models.Course;

public class CourseMapper {

  public static Course courseDTOToCourse(CourseDTO courseDTO) {
    Course course = new Course();
    course.setName(courseDTO.getName());
    return course;
  }
}
