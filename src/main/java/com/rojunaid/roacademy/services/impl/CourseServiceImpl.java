package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.controllers.CourseController;
import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.util.Helper;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired private CourseRepository courseRepository;

  @Autowired private GradeRepository gradeRepository;

  @Override
  public Iterable<CourseResponse> getAllCourse() {
    Iterable<Course> courses = courseRepository.findAll();
    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public CourseResponse createCourse(CourseRequest courseRequest) {
    Course course = this.courseDTOToCourse(courseRequest);
    course = courseRepository.save(course);
    return this.courseToCourseResponse(course);
  }

  @Override
  public CourseResponse updateCourse(Long courseId, CourseRequest courseRequest) {
    if (courseRepository.existsById(courseId)) {
      Course course = this.courseDTOToCourse(courseRequest);
      course.setId(courseId);
      course = courseRepository.save(course);
      return this.courseToCourseResponse(course);
    }
    throw this.courseNotFoundException(courseId);
  }

  @Override
  public CourseResponse findCourseById(Long courseId) {
    Course course =
        courseRepository
            .findById(courseId)
            .orElseThrow(() -> this.courseNotFoundException(courseId));

    return this.courseToCourseResponse(course);
  }

  @Override
  public void deleteCourseById(Long courseId) {

    if (courseRepository.existsById(courseId)) {
      courseRepository.deleteById(courseId);
    } else {
      throw this.courseNotFoundException(courseId);
    }
  }

  @Override
  public Iterable<CourseResponse> search(String query) {
    Iterable<Course> courses= courseRepository.search(query);
    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public CourseResponse courseToCourseResponse(Course course) {
    CourseResponse courseResponse = new CourseResponse();
    courseResponse.setId(course.getId());
    courseResponse.setName(course.getName());
    courseResponse.setGradeId(course.getGrade().getId());

    String url = Helper.buildURL(CourseController.class, "getCourseById", course.getId());

    courseResponse.setUrl(url);
    return courseResponse;
  }

  // util methods

  private Grade getGrade(Long gradeId) {
    return gradeRepository
        .findById(gradeId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Grade.id.notfound", new Object[] {gradeId})));
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
    return new ResourceNotFoundException(
        Translator.toLocale("Course.id.notfound", new Object[] {courseId}));
  }

  private Course courseDTOToCourse(CourseRequest courseRequest) {
    Course course = new Course();
    course.setName(courseRequest.getName());
    course.setGrade(this.getGrade(courseRequest.getGradeId()));
    course.setPreRequisiteCourses(
        this.getPreRequisiteCourses(courseRequest.getPreRequisiteCourseIds()));
    return course;
  }
}
