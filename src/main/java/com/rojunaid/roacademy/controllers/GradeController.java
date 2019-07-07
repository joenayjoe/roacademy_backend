package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories/{categoryId}/grades")
public class GradeController {

  @Autowired private GradeService gradeService;

  // GET /api/categories/:categoryId/grades
  // Get all grade
  @GetMapping("")
  public ResponseEntity<Iterable<Grade>> getAllGrade(@PathVariable Long categoryId) {
    Iterable<Grade> grades = gradeService.findGradesByCategoryId(categoryId);

    return new ResponseEntity<>(grades, HttpStatus.OK);
  }

  // POST /api/categories/:categoryId/grades
  // Create a grade
  @PostMapping("")
  public ResponseEntity<Grade> createGrade(
      @PathVariable Long categoryId, @Valid @RequestBody GradeDTO gradeDTO) {
    Grade persistentGrade = gradeService.createGrade(categoryId, gradeDTO);
    return new ResponseEntity<>(persistentGrade, HttpStatus.CREATED);
  }

  // PUT /api/grades/:gradeId
  // Update a grade
  @PutMapping("/{gradeId}")
  public ResponseEntity<Grade> updateGrade(
      @PathVariable Long categoryId, @PathVariable Long gradeId, @Valid @RequestBody GradeDTO gradeDTO) {
    Grade updatedGrade = gradeService.updateGrade(categoryId, gradeId, gradeDTO);
    return new ResponseEntity<>(updatedGrade, HttpStatus.OK);
  }

  // GET /api/grades/:gradeId
  // Get a grade by :gradeId

  @GetMapping("/{gradeId}")
  public ResponseEntity<Grade> getGradeById(@PathVariable Long gradeId) {
    Grade grade = gradeService.findGradeById(gradeId);
    return new ResponseEntity<>(grade, HttpStatus.OK);
  }

  // DELETE /api/grades/:gradeId
  // Delete a Grade by ID
  @DeleteMapping("/{gradeId}")
  public ResponseEntity<HttpStatus> deleteGradeById(@PathVariable Long gradeId) {
    gradeService.deleteGradeById(gradeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
