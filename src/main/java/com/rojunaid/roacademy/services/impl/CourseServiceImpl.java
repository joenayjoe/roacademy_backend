package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.auth.oauth2.UploadedResourceInfo;
import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.*;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.util.Constants;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired private CourseRepository courseRepository;

  @Autowired private GradeRepository gradeRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private FileUploadService fileUploadService;

  @Override
  public Page<CourseResponse> findAll(
      int page, int size, String order, List<CourseStatusEnum> status) {

    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.findAll(status, pageable);

    Page<CourseResponse> courseResponses =
        courses.map(course -> courseToCourseResponseWithObjectivesAndRequirements(course));
    return courseResponses;
  }

  @Override
  public Page<CourseResponse> findCoursesByCategoryId(
      Long category_id, int page, int size, List<CourseStatusEnum> status, String order) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.findAllByCategoryId(category_id, status, pageable);

    Page<CourseResponse> courseResponses = courses.map(course -> courseToCourseResponse(course));
    return courseResponses;
  }

  @Override
  public Page<CourseResponse> findCoursesByGradeId(
      Long grade_id, int page, int size, List<CourseStatusEnum> status, String order) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.findAllByGradeId(grade_id, status, pageable);

    Page<CourseResponse> courseResponses = courses.map(course -> courseToCourseResponse(course));
    return courseResponses;
  }

  @Override
  public Iterable<CourseResponse> findCoursesByGradeId(
      Long gradeId, List<CourseStatusEnum> status, String order) {
    Iterable<Course> courses =
        courseRepository.findAllByGradeId(gradeId, status, SortingUtils.SortBy(order));
    List<CourseResponse> responses = new ArrayList<>();
    for (Course course : courses) {
      responses.add(courseToCourseResponse(course));
    }
    return responses;
  }

  @Override
  @Transactional
  public CourseResponse createCourse(CourseRequest courseData, MultipartFile file) {
    Course course = this.courseRequestToCourse(courseData);
    CustomUserPrincipal principal =
        (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = principal.getUser();
    user = this.getUser(user.getId());

    if (file != null) {
      UploadedResourceInfo resourceInfo = fileUploadService.uploadToImgur(file);
      course.setImageUrl(resourceInfo.getResourceUrl());
      course.setImageId(resourceInfo.getResourceId());
    }
    course.addInstructor(user);
    course.addCreator(user);
    course = courseRepository.save(course);
    return this.courseToCourseResponse(course);
  }

  @Override
  @Transactional
  public CourseResponse updateCourse(
      Long courseId, CourseUpdateRequest courseData, MultipartFile file) {
    Course course = getCourse(courseData.getId());
    String oldImageId = course.getImageId();
    course = courseUpdateRequestToCourse(course, courseData);
    if (file != null) {
      UploadedResourceInfo resourceInfo = fileUploadService.uploadToImgur(file);
      if (oldImageId != null) {
        fileUploadService.deleteFromImgur(oldImageId);
      }
      course.setImageUrl(resourceInfo.getResourceUrl());
      course.setImageId(resourceInfo.getResourceId());
    }
    course = courseRepository.save(course);
    return this.courseToCourseResponse(course);
  }

  @Override
  public void updateStatus(Long courseId, CourseStatusUpdateRequest request) {
    Course course = getCourse(request.getId());
    course.setStatus(request.getStatus());
    courseRepository.save(course);
  }

  @Override
  public CourseResponse findCourseById(Long courseId, List<CourseStatusEnum> status) {
    Course course = getCourse(courseId, status);
    course.setHits(course.getHits() + 1);
    course = courseRepository.save(course);
    return this.courseToCourseResponseWithObjectivesAndRequirements(course);
  }

  @Override
  public void deleteCourseById(Long courseId) {

    Course course = getCourse(courseId);
    if (course.getStudents().size() > 0) {
      throw new BadRequestException(Translator.toLocale("Course.cannotdelete"));
    } else {
      courseRepository.deleteById(courseId);
    }
  }

  @Override
  public Page<SearchResponse> getAutocompleteSuggestionsForCourse(
      String query, int page, int size, String order, List<CourseStatusEnum> status) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.search(query, status, pageable);
    Page<SearchResponse> searchResponses = courses.map(course -> courseToSearchResponse(course));
    return searchResponses;
  }

  @Override
  public Page<CourseResponse> searchCoursesByKeyword(
      String query, int page, int size, String order, List<CourseStatusEnum> status) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<Course> courses = courseRepository.search(query, status, pageable);
    Page<CourseResponse> searchResponses = courses.map(course -> courseToCourseResponse(course));
    return searchResponses;
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

  @Override
  public Page<CourseResponse> findCoursesByInstructor(
      Long instructorId, int page, int size, List<CourseStatusEnum> statusEnums, String order) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(order));
    User instructor = getUser(instructorId);
    List<User> instructors = new ArrayList<>();
    instructors.add(instructor);

    Page<Course> courses =
        courseRepository.findCoursesByInstructorsInAndStatusIn(instructors, statusEnums, pageable);
    Page<CourseResponse> courseResponses = courses.map(course -> courseToCourseResponse(course));
    return courseResponses;
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

  private SearchResponse courseToSearchResponse(Course course) {
    SearchResponse searchResponse = new SearchResponse();
    searchResponse.setId(course.getId());
    searchResponse.setName(course.getName());
    searchResponse.setType(Course.class.getSimpleName());

    String url = "/api/courses/" + course.getId() + "?status=" + Constants.DEFAULT_COURSE_STATUS;
    searchResponse.setUrl(url);
    return searchResponse;
  }

  // private methods

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

  private Course getCourse(Long courseId) {
    return courseRepository
        .findById(courseId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Course.id.notfound", new Object[] {courseId})));
  }

  private Course getCourse(Long courseId, List<CourseStatusEnum> status) {
    return courseRepository
        .findById(courseId, status)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Course.id.notfound", new Object[] {courseId})));
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("User.id.notfound", new Object[] {userId})));
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
