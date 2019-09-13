package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CategoryService {
  Iterable<CategoryResponse> getAllCategory();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse createCategory(Category category);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse updateCategory(Long categoryId, Category category);

  CategoryResponse findCategoryById(Long categoryId);

  Iterable<GradeResponse> finGradesByCategoryId(Long category_id);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCategoryById(Long categoryId);
}
