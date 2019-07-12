package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.CourseDTO;
import com.rojunaid.roacademy.dto.mapper.CourseMapper;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired private CourseRepository courseRepository;

  @Autowired private GradeService gradeService;

  @Override
  public Iterable<Course> getAllCourse() {
    return courseRepository.findAll();
  }

  @Override
  public Course createCourse(CourseDTO courseDTO) {
    Course course = CourseMapper.courseDTOToCourse(courseDTO);
    course.setGrade(this.getGrade(courseDTO.getGradeId()));
    course.setPreRequisiteCourses(
        this.getPreRequisiteCourses(courseDTO.getPreRequisiteCourseIds()));
    return courseRepository.save(course);
  }

  @Override
  public Course updateCourse(Long courseId, CourseDTO courseDTO) {
    Course course = courseRepository.findById(courseId).orElse(null);
    if (course != null) {
      course.setPreRequisiteCourses(
          this.getPreRequisiteCourses(courseDTO.getPreRequisiteCourseIds()));

      course.setGrade(this.getGrade(courseDTO.getGradeId()));
      return courseRepository.save(course);
    }
    throw this.courseNotFoundException(courseId);
  }

  @Override
  public Course findCourseById(Long courseId) {
    return courseRepository
        .findById(courseId)
        .orElseThrow(() -> this.courseNotFoundException(courseId));
  }

  @Override
  public void deleteCourseById(Long courseId) {

    if (courseRepository.existsById(courseId)) {
      courseRepository.deleteById(courseId);
    } else {
      throw this.courseNotFoundException(courseId);
    }
  }

  // helper methods

  private Grade getGrade(Long gradeId) {
    return gradeService.findGradeById(gradeId);
  }

  private Set<Course> getPreRequisiteCourses(List<Long> preReqCourseIds) {
    Set<Course> preReqCourses = new HashSet<>();
    for (Long id : preReqCourseIds) {
      Course course1 =
          courseRepository.findById(id).orElseThrow(() -> this.courseNotFoundException(id));
      preReqCourses.add(course1);
    }
    return preReqCourses;
  }

  // private methods

  private ResourceNotFoundException courseNotFoundException(Long courseId) {
    return new ResourceNotFoundException("Course with id " + courseId + " not found");
  }
}
