package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.*;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired private CourseRepository courseRepository;

  @Autowired private GradeRepository gradeRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Override
  public Page<CourseResponse> findAll(
      int page, int size, String order, List<CourseStatusEnum> status) {

    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.findAll(status, pageable);

    Page<CourseResponse> courseResponses = courses.map(course -> courseToCourseResponse(course));
    return courseResponses;
  }

  @Override
  public Iterable<CourseResponse> findCoursesByCategoryId(
      Long category_id, List<CourseStatusEnum> status, String order) {
    Iterable<Course> courses =
        courseRepository.findAllByCategoryId(category_id, status, SortingUtils.SortBy(order));

    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public Iterable<CourseResponse> findCoursesByGradeId(
      Long grade_id, List<CourseStatusEnum> status, String order) {
    Iterable<Course> courses =
        courseRepository.findAllByGradeId(grade_id, status, SortingUtils.SortBy(order));

    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public CourseResponse createCourse(CourseRequest courseRequest) {
    Course course = this.courseRequestToCourse(courseRequest);
    course = courseRepository.save(course);
    return this.courseToCourseResponse(course);
  }

  @Override
  public CourseResponse updateCourse(Long courseId, CourseUpdateRequest courseRequest) {
    Course course = courseRepository.findById(courseRequest.getId()).orElse(null);
    if (course != null) {
      course = courseUpdateRequestToCourse(course, courseRequest);
      course = courseRepository.save(course);
      return this.courseToCourseResponse(course);
    }
    throw this.courseNotFoundException(courseId);
  }

  @Override
  public CourseResponse findCourseById(Long courseId, List<CourseStatusEnum> status) {
    List<String> statusList = status.stream().map(s -> s.name()).collect(Collectors.toList());
    Course course =
        courseRepository
            .findById(courseId, statusList)
            .orElseThrow(() -> this.courseNotFoundException(courseId));

    return this.courseToCourseResponseWithObjectivesAndRequirements(course);
  }

  @Override
  public void deleteCourseById(Long courseId) {

    Course course = courseRepository.findById(courseId).orElse(null);
    if (course != null) {
      if (course.getStudents().size() > 0) {
        throw new BadRequestException(Translator.toLocale("Course.cannotdelete"));
      } else {
        courseRepository.deleteById(courseId);
      }
    }
    throw this.courseNotFoundException(courseId);
  }

  @Override
  public Iterable<CourseResponse> search(String query) {
    Iterable<Course> courses = courseRepository.search(query);
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
    courseResponse.setHeadline(course.getHeadline());
    courseResponse.setDescription(course.getDescription());
    courseResponse.setCreatedAt(course.getCreatedAt());
    courseResponse.setUpdatedAt(course.getUpdatedAt());

    if (course.getGrade() != null) {
      PrimaryGrade primaryGrade = new PrimaryGrade();
      primaryGrade.setId(course.getGrade().getId());
      primaryGrade.setName(course.getGrade().getName());
      courseResponse.setPrimaryGrade(primaryGrade);
    }

    if (course.getCategory() != null) {
      PrimaryCategory primaryCategory = new PrimaryCategory();
      primaryCategory.setId(course.getCategory().getId());
      primaryCategory.setName(course.getCategory().getName());
      courseResponse.setPrimaryCategory(primaryCategory);
    }

    courseResponse.setLevel(course.getLevel().name());
    courseResponse.setHits(course.getHits());

    PrimaryUser primaryUser = new PrimaryUser();
    primaryUser.setId(course.getCreatedBy().getId());
    primaryUser.setFirstName(course.getCreatedBy().getFirstName());
    primaryUser.setLastName(course.getCreatedBy().getLastName());
    primaryUser.setEmail(course.getCreatedBy().getEmail());

    courseResponse.setCreatedBy(primaryUser);

    courseResponse.setImageUrl(course.getImageUrl());
    courseResponse.setStatus(course.getStatus().name());
    return courseResponse;
  }

  // util methods

  private CourseResponse courseToCourseResponseWithObjectivesAndRequirements(Course course) {
    CourseResponse courseResponse = this.courseToCourseResponse(course);

    courseResponse.setObjectives(
        course.getObjectives().stream().map(x -> x.getName()).collect(Collectors.toList()));
    courseResponse.setRequirements(
        course.getCourseRequirements().stream().map(x -> x.getName()).collect(Collectors.toList()));
    return courseResponse;
  }

  private Grade getGrade(Long gradeId) {
    return gradeRepository
        .findById(gradeId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Grade.id.notfound", new Object[] {gradeId})));
  }

  private Category getCategory(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Category.id.notfound", new Object[] {categoryId})));
  }

  // private methods

  private ResourceNotFoundException courseNotFoundException(Long courseId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Course.id.notfound", new Object[] {courseId}));
  }

  private Course courseRequestToCourse(CourseRequest courseRequest) {
    Course course = new Course();
    course.setName(courseRequest.getName());
    course.setHeadline(courseRequest.getHeadline());
    course.setDescription(courseRequest.getDescription());
    if (courseRequest.getCategoryId() != null) {
      course.addCategory(this.getCategory(courseRequest.getCategoryId()));
    }
    if (courseRequest.getGradeId() != null) {
      course.addGrade(this.getGrade(courseRequest.getGradeId()));
    }

    course.setLevel(courseRequest.getLevel());

    // save objectives
    for (String objectiveName : courseRequest.getObjectives()) {
      CourseObjective courseObjective = new CourseObjective();
      courseObjective.setName(objectiveName);
      course.addCourseObjective(courseObjective);
    }

    // save requirements
    for (String requirement : courseRequest.getRequirements()) {
      CourseRequirement courseRequirement = new CourseRequirement();
      courseRequirement.setName(requirement);
      course.addCourseRequirement(courseRequirement);
    }
    return course;
  }

  private Course courseUpdateRequestToCourse(Course course, CourseUpdateRequest updateRequest) {

    course.setId(updateRequest.getId());
    course.setName(updateRequest.getName());
    course.setHeadline(updateRequest.getHeadline());
    course.setDescription(updateRequest.getDescription());

    if (updateRequest.getCategoryId() != null) {
      course.addCategory(this.getCategory(updateRequest.getCategoryId()));
    }

    if (updateRequest.getGradeId() != null) {
      course.addGrade(this.getGrade(updateRequest.getGradeId()));
    }

    course.setLevel(updateRequest.getLevel());

    // save objectives
    course.getObjectives().clear();
    for (String objectiveName : updateRequest.getObjectives()) {

      CourseObjective courseObjective = new CourseObjective();
      courseObjective.setName(objectiveName);
      course.addCourseObjective(courseObjective);
    }

    // save requirements
    course.getCourseRequirements().clear();
    for (String requirement : updateRequest.getRequirements()) {
      CourseRequirement courseRequirement = new CourseRequirement();
      courseRequirement.setName(requirement);
      course.addCourseRequirement(courseRequirement);
    }
    return course;
  }
}
