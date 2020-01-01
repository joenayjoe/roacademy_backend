package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.dto.GradeUpdateRequest;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.GradeService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

  @Autowired GradeRepository gradeRepository;
  @Autowired CategoryRepository categoryRepository;
  @Autowired CourseRepository courseRepository;
  @Autowired CourseService courseService;

  @Override
  public Iterable<Grade> getAllGrade() {
    return gradeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  @Override
  public GradeResponse createGrade(Long categoryId, GradeRequest gradeRequest) {
    Category category = categoryRepository.findById(gradeRequest.getCategoryId()).orElse(null);
    if (category != null) {
      Grade grade = this.gradeRequestToGrade(gradeRequest);
      grade.setCategory(category);
      grade = gradeRepository.save(grade);

      GradeResponse gradeResponse = this.gradeToGradeResponse(grade);
      return gradeResponse;
    }
    throw new ResourceNotFoundException(
        "Category with ID [" + gradeRequest.getCategoryId() + "] not found");
  }

  @Override
  public GradeResponse updateGrade(
      Long categoryId, Long gradeId, GradeUpdateRequest gradeUpdateRequest) {
    Category category =
        categoryRepository
            .findById(gradeUpdateRequest.getCategoryId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Category with ID " + gradeUpdateRequest.getCategoryId() + " not found."));
    Grade grade = gradeRepository.findById(gradeUpdateRequest.getId()).orElse(null);
    if (grade != null) {
      grade.setName(gradeUpdateRequest.getName());
      grade.setCategory(category);
      grade = gradeRepository.save(grade);
      GradeResponse gradeResponse = this.gradeToGradeResponse(grade);
      return gradeResponse;
    }
    throw this.gradeNotFoundException(gradeId);
  }

  @Override
  public GradeResponse findGradeById(Long gradeId) {
    Grade grade =
        gradeRepository.findById(gradeId).orElseThrow(() -> this.gradeNotFoundException(gradeId));

    GradeResponse gradeResponse = this.gradeToGradeResponse(grade);
    return gradeResponse;
  }

  @Override
  public GradeResponse findGradeWithCoursesById(Long gradeId) {
    Grade grade =
        gradeRepository
            .findGradeWithCoursesById(gradeId)
            .orElseThrow(() -> this.gradeNotFoundException(gradeId));
    GradeResponse gradeResponse = this.gradeToGradeResponseWithCourses(grade);
    return gradeResponse;
  }

  @Override
  public Iterable<GradeResponse> findGradesByCategoryId(Long categoryId, String order) {
    Iterable<Grade> grades =
        gradeRepository.findAllByCategoryId(categoryId, SortingUtils.SortBy(order));
    List<GradeResponse> gradeResponses = new ArrayList<>();
    for (Grade grade : grades) {
      gradeResponses.add(this.gradeToGradeResponse(grade));
    }
    return gradeResponses;
  }

  @Override
  public void deleteGradeById(Long gradeId) {
    Grade grade = gradeRepository.findById(gradeId).orElse(null);
    if (grade != null) {
      if (grade.getCourses().size() > 0) {
        throw new BadRequestException(
            "Grade with associated courses cannot be deleted. Delete associated courses first.");
      }
      gradeRepository.deleteById(gradeId);
    } else {
      throw this.gradeNotFoundException(gradeId);
    }
  }

  @Override
  public Iterable<CourseResponse> findCoursesByGradeId(Long gradeId) {
    Iterable<Course> courses = courseRepository.findAllByGradeId(gradeId);
    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : courses) {
      courseResponses.add(courseService.courseToCourseResponse(course));
    }
    return courseResponses;
  }

  @Override
  public GradeResponse gradeToGradeResponse(Grade grade) {
    GradeResponse gradeResponse = new GradeResponse();
    gradeResponse.setId(grade.getId());
    gradeResponse.setName(grade.getName());
    gradeResponse.setCategoryId(grade.getCategory().getId());
    gradeResponse.setCreatedAt(grade.getCreatedAt());
    gradeResponse.setUpdatedAt(grade.getUpdatedAt());

    return gradeResponse;
  }

  // private methods
  private GradeResponse gradeToGradeResponseWithCourses(Grade grade) {
    GradeResponse gradeResponse = gradeToGradeResponse(grade);
    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Course course : grade.getCourses()) {
      courseResponses.add(courseService.courseToCourseResponse(course));
    }
    gradeResponse.setCourses(courseResponses);
    return gradeResponse;
  }

  private ResourceNotFoundException gradeNotFoundException(Long gradeId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Grade.id.notfound", new Object[] {gradeId}));
  }

  private Grade gradeRequestToGrade(GradeRequest gradeRequest) {
    Grade grade = new Grade();
    grade.setName(gradeRequest.getName());
    return grade;
  }
}
