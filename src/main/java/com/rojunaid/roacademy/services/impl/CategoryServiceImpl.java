package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.CategoryRequest;
import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.dto.CategoryUpdateRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.CategoryRepository;
import com.rojunaid.roacademy.services.CategoryService;
import com.rojunaid.roacademy.services.GradeService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;

  @Autowired private GradeService gradeService;

  @Override
  public Iterable<CategoryResponse> findAllCategory(String order) {

    Iterable<Category> categories = categoryRepository.findAll(SortingUtils.SortBy(order));
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    for (Category category : categories) {
      categoryResponses.add(this.categoryToCategoryResponse(category));
    }
    return categoryResponses;
  }

  @Override
  public Iterable<CategoryResponse> findAllCategoryWithGrades(String order) {
    Iterable<Category> categories =
        categoryRepository.findAllWithGrades(SortingUtils.SortBy(order));
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    for (Category category : categories) {
      categoryResponses.add(this.categoryToCategoryResponseWithGrades(category));
    }
    return categoryResponses;
  }

  @Override
  public CategoryResponse createCategory(CategoryRequest category) {
    Category category1 = new Category();
    category1.setName(category.getName());
    category1 = categoryRepository.save(category1);
    return this.categoryToCategoryResponse(category1);
  }

  @Override
  public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest updatedCategory) {
    Category category = categoryRepository.findById(updatedCategory.getId()).orElse(null);
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
  public void deleteCategoryById(Long categoryId) {
    Category category = categoryRepository.findById(categoryId).orElse(null);
    if (category != null) {
      if (category.getGrades().size() > 0) {
        throw new BadRequestException(Translator.toLocale("Category.cannotdelete"));
      }
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

  private CategoryResponse categoryToCategoryResponse(Category category) {
    CategoryResponse categoryResponse = new CategoryResponse();
    categoryResponse.setId(category.getId());
    categoryResponse.setName(category.getName());
    categoryResponse.setCreatedAt(category.getCreatedAt());
    categoryResponse.setUpdatedAt(category.getUpdatedAt());

    return categoryResponse;
  }
}
