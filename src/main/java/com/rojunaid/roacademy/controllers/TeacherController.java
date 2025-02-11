package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

  @Autowired private UserService userService;

  @GetMapping("/{teacherId}/courses")
  public ResponseEntity<Page<CourseResponse>> teacherCourses(
      @PathVariable Long teacherId,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order) {

    Page<CourseResponse> courseResponses =
        userService.getTeachingCourses(teacherId, page, size, status, order);
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }
}
