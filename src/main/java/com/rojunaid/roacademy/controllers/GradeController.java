package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.dto.GradeResponse;
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
  // Get all grades
  @GetMapping("")
  public ResponseEntity<Iterable<GradeResponse>> getAllGrade(@PathVariable Long categoryId) {
    Iterable<GradeResponse> gradeResponses = gradeService.findGradesByCategoryId(categoryId);

    return new ResponseEntity<>(gradeResponses, HttpStatus.OK);
  }

  // POST /api/categories/:categoryId/grades
  // Create a grade
  @PostMapping("")
  public ResponseEntity<GradeResponse> createGrade(
      @PathVariable Long categoryId, @Valid @RequestBody GradeDTO gradeDTO) {
    GradeResponse gradeResponse = gradeService.createGrade(categoryId, gradeDTO);
    return new ResponseEntity<>(gradeResponse, HttpStatus.CREATED);
  }

  // PUT /api/sections/:gradeId
  // Update a section
  @PutMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> updateGrade(
      @PathVariable Long categoryId,
      @PathVariable Long gradeId,
      @Valid @RequestBody GradeDTO gradeDTO) {
    GradeResponse gradeResponse = gradeService.updateGrade(categoryId, gradeId, gradeDTO);
    return new ResponseEntity<>(gradeResponse, HttpStatus.OK);
  }

  // GET /api/sections/:gradeId
  // Get a section by :gradeId

  @GetMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> getGradeById(@PathVariable Long gradeId) {
    GradeResponse gradeResponse = gradeService.findGradeById(gradeId);
    return new ResponseEntity<>(gradeResponse, HttpStatus.OK);
  }

  // DELETE /api/sections/:gradeId
  // Delete a Grade by ID
  @DeleteMapping("/{gradeId}")
  public ResponseEntity<HttpStatus> deleteGradeById(@PathVariable Long gradeId) {
    gradeService.deleteGradeById(gradeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{gradeId}/courses")
  public ResponseEntity<Iterable<CourseResponse>> getCoursesByGradeId(@PathVariable Long gradeId) {

    Iterable<CourseResponse> courseResponses = gradeService.findCoursesByGradeId(gradeId);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }
}
