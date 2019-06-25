package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

  @Autowired private GradeService gradeService;

  // GET /api/grades
  // Get all grade
  @GetMapping("")
  public ResponseEntity<Iterable<Grade>> getAllGrade() {
    Iterable<Grade> grades = gradeService.getAllGrade();

    return new ResponseEntity<>(grades, HttpStatus.OK);
  }

  // POST /api/grades
  // Create a grade
  @PostMapping("")
  public ResponseEntity<Grade> createGrade(@Valid @RequestBody Grade grade) {
    Grade grade1 = gradeService.createGrade(grade);
    return new ResponseEntity<>(grade1, HttpStatus.CREATED);
  }

  // PUT /api/grades/:gradeId
  // Update a grade
  @PutMapping("/{gradeId}")
  public ResponseEntity<Grade> updateGrade(@PathVariable Long gradeId, @RequestBody Grade grade) {
    if (gradeService.isGradeExist(gradeId)) {
      Grade updatedGrade = gradeService.updateGrade(grade);

      return new ResponseEntity<>(updatedGrade, HttpStatus.OK);
    } else {
      throw new ResourceNotFoundException("Grade with id "+gradeId+" not found");
    }
  }

  // GET /api/grades/:gradeId
  // Get a grade by :gradeId

  @GetMapping("/{gradeId}")
  public ResponseEntity<Grade> getGradeById(@PathVariable Long gradeId) {
    Grade grade =
        gradeService.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException("Grade with id "+gradeId+" not found"));
    return new ResponseEntity<>(grade, HttpStatus.OK);
  }
}
