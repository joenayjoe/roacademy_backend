package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.models.Category;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CategoryService {
  Iterable<CategoryResponse> getAllCategory(String order);
  Iterable<CategoryResponse> getAllCategoryWithGrades(String order);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse createCategory(Category category);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse updateCategory(Long categoryId, Category category);

  CategoryResponse findCategoryById(Long categoryId);

  CategoryResponse finCategoryWithGradesById(Long categoryId);

  List<CourseResponse> findCoursesForCategory(Long categoryId);

  Iterable<GradeResponse> finGradesByCategoryId(Long category_id);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCategoryById(Long categoryId);
}
