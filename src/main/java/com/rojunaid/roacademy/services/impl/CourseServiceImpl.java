package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.CourseRequest;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.*;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired private CourseRepository courseRepository;

  @Autowired private GradeRepository gradeRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private UserService userService;

  @Override
  public Iterable<CourseResponse> findAll(String order) {

    Iterable<Course> courses = courseRepository.findAll(SortingUtils.SortBy(order));

    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public Iterable<CourseResponse> findCoursesByCategoryId(
      Long category_id, CourseStatusEnum[] statuses, String order) {

    List<String> statusList = new ArrayList<>();
    for (CourseStatusEnum st : statuses) {
      statusList.add(st.name());
    }
    Iterable<Course> courses =
        courseRepository.findAllByCategoryId(category_id, statusList, SortingUtils.SortBy(order));

    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(this.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public Iterable<CourseResponse> findCoursesByGradeId(
      Long grade_id, CourseStatusEnum[] statuses, String order) {
    List<String> statusList = new ArrayList<>();
    for (CourseStatusEnum st : statuses) {
      statusList.add(st.name());
    }
    Iterable<Course> courses =
        courseRepository.findAllByGradeId(grade_id, statusList, SortingUtils.SortBy(order));

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
  public CourseResponse updateCourse(Long courseId, CourseRequest courseRequest) {
    if (courseRepository.existsById(courseId)) {
      Course course = this.courseRequestToCourse(courseRequest);
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

    return this.courseToCourseResponseWithObjectivesAndRequirements(course);
  }

  @Override
  public void deleteCourseById(Long courseId) {

    Course course = courseRepository.findById(courseId).orElse(null);
    if (course != null) {
      if (course.getStudents().size() > 0) {
        throw new BadRequestException(
            "This course has enrolled students, therefore cannot be deleted.");
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
      courseResponse.setGradeId(course.getGrade().getId());
    }

    if (course.getCategory() != null) {
      courseResponse.setCategoryId(course.getCategory().getId());
    }

    courseResponse.setLevel(course.getLevel().name());
    courseResponse.setHits(course.getHits());
    courseResponse.setCreatedBy(this.userService.userToUserResponse(course.getCreatedBy()));
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
    course.setPreRequisiteCourses(
        this.getPreRequisiteCourses(courseRequest.getPreRequisiteCourseIds()));

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
}
