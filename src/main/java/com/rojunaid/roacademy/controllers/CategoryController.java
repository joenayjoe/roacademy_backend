package com.rojunaid.roacademy.controllers;

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
  public ResponseEntity<Iterable<Category>> getAllCategory() {
    Iterable<Category> caregories = categoryService.getAllCategory();
    return new ResponseEntity<>(caregories, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
    Category category1 = categoryService.createCategory(category);
    return new ResponseEntity<>(category1, HttpStatus.CREATED);
  }

  @PutMapping("/{category_id}")
  public ResponseEntity<Category> updateCategory(
      @Valid @RequestBody Category category, @PathVariable Long category_id) {
    Category category1 = categoryService.updateCategory(category_id, category);
    return new ResponseEntity<>(category1, HttpStatus.OK);
  }

  @GetMapping("/{category_id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long category_id) {
    Category category = categoryService.findCategoryById(category_id);
    return new ResponseEntity<>(category, HttpStatus.OK);
  }

  // this method is implemented in grade controller
  //  @GetMapping("/{category_id}/grades")
  //  public ResponseEntity<Iterable<Grade>> getGradesByCategory(@PathVariable Long category_id) {
  //    Iterable<Grade> grades = categoryService.finGradesByCategoryId(category_id);
  //    return new ResponseEntity<>(grades, HttpStatus.OK);
  //  }

  @DeleteMapping("/{category_id}")
  public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long category_id) {
    categoryService.deleteCategoryById(category_id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
