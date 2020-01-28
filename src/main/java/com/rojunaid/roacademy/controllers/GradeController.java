package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.GradeRequest;
import com.rojunaid.roacademy.dto.GradeResponse;
import com.rojunaid.roacademy.dto.GradeUpdateRequest;
import com.rojunaid.roacademy.services.GradeService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

  @Autowired private GradeService gradeService;

  // GET /api/grades
  // Get all grades
  @GetMapping("")
  public ResponseEntity<Iterable<GradeResponse>> getAllGrades(
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<GradeResponse> gradeResponses = gradeService.findAll(page, size, order);

    return new ResponseEntity<>(gradeResponses, HttpStatus.OK);
  }

  // GET /api/grades?category_id=:categoryId
  // Get all grades by categoryId
  @GetMapping(value = "", params = "category_id")
  public ResponseEntity<Iterable<GradeResponse>> getGradesByCategoryId(
      @RequestParam Long category_id,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Iterable<GradeResponse> gradeResponses =
        gradeService.findGradesByCategoryId(category_id, order);

    return new ResponseEntity<>(gradeResponses, HttpStatus.OK);
  }

  // POST /api/categories/:categoryId/grades
  // Create a grade
  @PostMapping("")
  public ResponseEntity<GradeResponse> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
    GradeResponse gradeResponse = gradeService.createGrade(gradeRequest);
    return new ResponseEntity<>(gradeResponse, HttpStatus.CREATED);
  }

  // PUT /api/sections/:gradeId
  // Update a section
  @PutMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> updateGrade(
      @PathVariable Long gradeId,
      @Valid @RequestBody GradeUpdateRequest gradeUpdateRequest) {
    GradeResponse gradeResponse = gradeService.updateGrade(gradeId, gradeUpdateRequest);
    return new ResponseEntity<>(gradeResponse, HttpStatus.OK);
  }

  // GET /api/sections/:gradeId
  // Get a section by :gradeId

  @GetMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> getGradeById(
      @PathVariable Long gradeId, @RequestParam(defaultValue = "false") boolean withCourse) {

    GradeResponse gradeResponse;
    if (withCourse) {
      gradeResponse = gradeService.findGradeWithCoursesById(gradeId);
    } else {
      gradeResponse = gradeService.findGradeById(gradeId);
    }

    return new ResponseEntity<>(gradeResponse, HttpStatus.OK);
  }

  // DELETE /api/sections/:gradeId
  // Delete a Grade by ID
  @DeleteMapping("/{gradeId}")
  public ResponseEntity<HttpStatus> deleteGradeById(@PathVariable Long gradeId) {
    gradeService.deleteGradeById(gradeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
