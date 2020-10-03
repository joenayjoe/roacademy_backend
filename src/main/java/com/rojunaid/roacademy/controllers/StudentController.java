package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.CourseSubscriptionCheckResponse;
import com.rojunaid.roacademy.dto.CourseSubscriptionRequest;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

  private final UserService userService;

  @Autowired
  public StudentController(UserService userService) {
    this.userService = userService;
  }

  @PutMapping("/{studentId}/course_subscription")
  public ResponseEntity<HttpStatus> subscribeCourse(
      @PathVariable Long studentId, @Valid @RequestBody CourseSubscriptionRequest subscription) {
    userService.subscribeCourse(studentId, subscription);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{userId}/courses/{courseId}/is_subscribed")
  public ResponseEntity<CourseSubscriptionCheckResponse> isSubscribed(
      @PathVariable Long userId, @PathVariable Long courseId) {
    CourseSubscriptionCheckResponse r = userService.isSubscribed(userId, courseId);
    return new ResponseEntity<>(r, HttpStatus.OK);
  }

  @GetMapping("/{studentId}/courses")
  public ResponseEntity<Page<CourseResponse>> getSubscribedCourses(
      @PathVariable Long studentId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = "createdAt_desc") String order) {
    Page<CourseResponse> responses = userService.getSubscribedCourses(studentId, page, size, order);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }
}
