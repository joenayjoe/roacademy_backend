package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.services.CategoryService;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.GradeService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private GradeService gradeService;

  @Autowired private CourseService courseService;

  @Override
  public Iterable<CategoryResponse> getAllCategory() {
    Iterable<Category> categories = categoryRepository.findAll();
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    for (Category category : categories) {
      categoryResponses.add(this.categoryToCategoryResponse(category));
    }
    return categoryResponses;
  }

  @Override
  public Iterable<CategoryResponse> getAllCategoryWithGrades() {
    Iterable<Category> categories = categoryRepository.findAllWithGrades();
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    for (Category category : categories) {
      categoryResponses.add(this.categoryToCategoryResponseWithGrades(category));
    }
    return categoryResponses;
  }

  @Override
  public CategoryResponse createCategory(Category category) {
    Category category1 = categoryRepository.save(category);
    return this.categoryToCategoryResponse(category1);
  }

  @Override
  public CategoryResponse updateCategory(Long categoryId, Category updatedCategory) {
    Category category = categoryRepository.findById(categoryId).orElse(null);
    if (category != null) {
      category.setName(updatedCategory.getName());
      category = categoryRepository.save(category);
      return this.categoryToCategoryResponse(category);
    }
    throw this.categoryNotFoundException(categoryId);
  }

  @Override
  public CategoryResponse findCategoryById(Long categoryId) {
    Category category =
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> this.categoryNotFoundException(categoryId));
    return this.categoryToCategoryResponse(category);
  }

  @Override
  public CategoryResponse finCategoryWithGradesById(Long categoryId) {
    Category category =
        categoryRepository
            .finCategoryWithGradesById(categoryId)
            .orElseThrow(() -> this.categoryNotFoundException(categoryId));
    return this.categoryToCategoryResponseWithGrades(category);
  }

  @Override
  public List<CourseResponse> findCoursesForCategory(Long categoryId) {
    Category category =
        categoryRepository
            .finCategoryWithGradesAndCoursesById(categoryId)
            .orElseThrow(() -> this.categoryNotFoundException(categoryId));

    return this.extractCourseResponsesFromCategory(category);
  }

  @Override
  public Iterable<GradeResponse> finGradesByCategoryId(Long category_id) {
    return gradeService.findGradesByCategoryId(category_id);
  }

  @Override
  public void deleteCategoryById(Long categoryId) {
    if (categoryRepository.existsById(categoryId)) {
      categoryRepository.deleteById(categoryId);
    } else {
      this.categoryNotFoundException(categoryId);
    }
  }

  // private methods

  private ResourceNotFoundException categoryNotFoundException(Long category_id) {
    return new ResourceNotFoundException(
        Translator.toLocale("Category.id.notfound", new Object[] {category_id}));
  }

  private CategoryResponse categoryToCategoryResponseWithGrades(Category category) {
    CategoryResponse categoryResponse = categoryToCategoryResponse(category);

    List<GradeResponse> gradeResponses = new ArrayList<>();
    for (Grade grade : category.getGrades()) {
      gradeResponses.add(this.gradeService.gradeToGradeResponse(grade));
    }
    categoryResponse.setGrades(gradeResponses);
    return categoryResponse;
  }

  private List<CourseResponse> extractCourseResponsesFromCategory(Category category) {

    List<CourseResponse> courseResponses = new ArrayList<>();
    for (Grade grade : category.getGrades()) {
      for (Course course : grade.getCourses()) {
        courseResponses.add(this.courseService.courseToCourseResponse(course));
      }
    }

    return courseResponses;
  }

  private CategoryResponse categoryToCategoryResponse(Category category) {
    CategoryResponse categoryResponse = new CategoryResponse();
    categoryResponse.setId(category.getId());
    categoryResponse.setName(category.getName());

    return categoryResponse;
  }
}
