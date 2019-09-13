package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.controllers.GradeController;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.repositories.CourseRepository;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.GradeService;
import com.rojunaid.roacademy.util.Helper;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  public GradeResponse createGrade(Long categoryId, GradeDTO gradeDTO) {
    Category category = categoryRepository.findById(categoryId).orElse(null);
    Grade grade = this.gradeDTOToGrade(gradeDTO);
    grade.setCategory(category);
    grade = gradeRepository.save(grade);

    GradeResponse gradeResponse = this.gradeToGradeResponse(grade);
    return gradeResponse;
  }

  @Override
  public GradeResponse updateGrade(Long categoryId, Long gradeId, GradeDTO gradeDTO) {
    Category category = categoryRepository.findById(categoryId).orElse(null);
    if (gradeRepository.existsById(gradeId)) {
      Grade grade = this.gradeDTOToGrade(gradeDTO);
      grade.setCategory(category);
      grade.setId(gradeId);
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
  public Iterable<GradeResponse> findGradesByCategoryId(Long categoryId) {
    Iterable<Grade> grades = gradeRepository.findAllByCategoryId(categoryId);
    List<GradeResponse> gradeResponses = new ArrayList<>();
    for (Grade grade : grades) {
      gradeResponses.add(this.gradeToGradeResponse(grade));
    }
    return gradeResponses;
  }

  @Override
  public void deleteGradeById(Long gradeId) {
    if (gradeRepository.existsById(gradeId)) {
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

    String url =
        Helper.buildURL(
            GradeController.class, "getGradeById", grade.getId(), grade.getCategory().getId());
    gradeResponse.setUrl(url);
    return gradeResponse;
  }

  // private methods

  private ResourceNotFoundException gradeNotFoundException(Long gradeId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Grade.id.notfound", new Object[] {gradeId}));
  }

  private Grade gradeDTOToGrade(GradeDTO gradeDTO) {
    Grade grade = new Grade();
    grade.setName(gradeDTO.getName());
    return grade;
  }
}
