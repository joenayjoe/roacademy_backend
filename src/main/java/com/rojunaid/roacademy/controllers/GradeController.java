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

  // GET /api/categories/:categoryId/sections
  // Get all section
  @GetMapping("")
  public ResponseEntity<Iterable<Grade>> getAllGrade(@PathVariable Long categoryId) {
    Iterable<Grade> grades = gradeService.findGradesByCategoryId(categoryId);

    return new ResponseEntity<>(grades, HttpStatus.OK);
  }

  // POST /api/categories/:categoryId/sections
  // Create a section
  @PostMapping("")
  public ResponseEntity<Grade> createGrade(
      @PathVariable Long categoryId, @Valid @RequestBody GradeDTO gradeDTO) {
    Grade persistentSection = gradeService.createGrade(categoryId, gradeDTO);
    return new ResponseEntity<>(persistentSection, HttpStatus.CREATED);
  }

  // PUT /api/sections/:gradeId
  // Update a section
  @PutMapping("/{gradeId}")
  public ResponseEntity<Grade> updateGrade(
      @PathVariable Long categoryId,
      @PathVariable Long gradeId,
      @Valid @RequestBody GradeDTO gradeDTO) {
    Grade updatedSection = gradeService.updateGrade(categoryId, gradeId, gradeDTO);
    return new ResponseEntity<>(updatedSection, HttpStatus.OK);
  }

  // GET /api/sections/:gradeId
  // Get a section by :gradeId

  @GetMapping("/{gradeId}")
  public ResponseEntity<Grade> getGradeById(@PathVariable Long gradeId) {
    Grade section = gradeService.findGradeById(gradeId);
    return new ResponseEntity<>(section, HttpStatus.OK);
  }

  // DELETE /api/sections/:gradeId
  // Delete a Grade by ID
  @DeleteMapping("/{gradeId}")
  public ResponseEntity<HttpStatus> deleteGradeById(@PathVariable Long gradeId) {
    gradeService.deleteGradeById(gradeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
