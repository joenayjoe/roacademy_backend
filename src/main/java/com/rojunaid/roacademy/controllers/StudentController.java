package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

  private final UserService userService;

  @Autowired
  public StudentController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{studentId}/courses")
  public ResponseEntity<Page<CourseResponse>> getSubscribedCourses(
      @PathVariable Long studentId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {
    Page<CourseResponse> responses = userService.getSubscribedCourses(studentId, page, size, order);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }
}
