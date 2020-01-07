package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CategoryResponse;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  @Autowired private CategoryService categoryService;

  @GetMapping("")
  public ResponseEntity<Iterable<CategoryResponse>> getAllCategory(
      @RequestParam(value = "withGrade", required = false, defaultValue = "false")
          boolean withGrade,
      @RequestParam(value = "order", required = false, defaultValue = "id_asc") String order) {
    if (!withGrade) {
      Iterable<CategoryResponse> categoryResponses = categoryService.findAllCategory(order);
      return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    } else {
      Iterable<CategoryResponse> categoryResponses =
          categoryService.findAllCategoryWithGrades(order);
      return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }
  }

  @PostMapping("")
  public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody Category category) {
    CategoryResponse categoryResponse = categoryService.createCategory(category);
    return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{category_id}")
  public ResponseEntity<CategoryResponse> updateCategory(
      @Valid @RequestBody Category category, @PathVariable Long category_id) {
    CategoryResponse categoryResponse = categoryService.updateCategory(category_id, category);
    return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
  }

  @GetMapping("/{category_id}")
  public ResponseEntity<CategoryResponse> getCategoryById(
      @PathVariable Long category_id,
      @RequestParam(value = "withGrade", required = false, defaultValue = "false")
          boolean withGrade) {

    if (withGrade) {
      CategoryResponse categoryResponse = categoryService.finCategoryWithGradesById(category_id);
      return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    } else {
      CategoryResponse categoryResponse = categoryService.findCategoryById(category_id);
      return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
  }

  @DeleteMapping("/{category_id}")
  public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long category_id) {
    categoryService.deleteCategoryById(category_id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
