package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CategoryService {
  Iterable<Category> getAllCategory();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Category createCategory(Category category);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Category updateCategory(Long categoryId, Category category);

  Category findCategoryById(Long categoryId);

  Iterable<Grade> finGradesByCategoryId(Long category_id);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteCategoryById(Long categoryId);
}
