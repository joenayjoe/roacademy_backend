package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.CategoryRequest;
import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.dto.CategoryUpdateRequest;
import com.rojunaid.roacademy.models.Category;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CategoryService {
  Iterable<CategoryResponse> findAllCategory(String order);

  Iterable<CategoryResponse> findAllCategoryWithGrades(String order);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse createCategory(CategoryRequest category);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest category);

  CategoryResponse findCategoryById(Long categoryId);

  CategoryResponse finCategoryWithGradesById(Long categoryId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCategoryById(Long categoryId);
}
