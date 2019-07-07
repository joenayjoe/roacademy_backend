package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;

public interface CategoryService {
  Iterable<Category> getAllCategory();

  Category createCategory(Category category);

  Category updateCategory(Long categoryId, Category category);

  Category findCategoryById(Long categoryId);

  Iterable<Grade> finGradesByCategoryId(Long category_id);

  void deleteCategoryById(Long categoryId);
}
