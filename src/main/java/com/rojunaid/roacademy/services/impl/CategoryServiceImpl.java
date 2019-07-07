package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.services.CategoryService;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private GradeService gradeService;

  @Override
  public Iterable<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  @Override
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategory(Long categoryId, Category category) {
    if (categoryRepository.existsById(categoryId)) {
      category.setId(categoryId);
      return categoryRepository.save(category);
    }
    throw this.categoryNotFoundException(categoryId);
  }

  @Override
  public Category findCategoryById(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> this.categoryNotFoundException(categoryId));
  }

  @Override
  public Iterable<Grade> finGradesByCategoryId(Long category_id) {
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
    return new ResourceNotFoundException("Category with ID " + category_id + " not found");
  }
}
