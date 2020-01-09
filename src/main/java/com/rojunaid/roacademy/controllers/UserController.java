package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.ResetPasswordRequest;
import com.rojunaid.roacademy.dto.UserResponse;
import com.rojunaid.roacademy.dto.UserRoleUpdateRequest;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private UserService userService;
  @Autowired private FileUploadService fileUploadService;

  @GetMapping("")
  public ResponseEntity<Iterable<UserResponse>> getAllUsers() {
    Iterable<UserResponse> userResponses = userService.findAll();
    return new ResponseEntity<>(userResponses, HttpStatus.OK);
  }

  @PostMapping("/{userId}/update_roles")
  public ResponseEntity<UserResponse> updateUserRoles(
      @PathVariable Long userId, @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
    UserResponse userResponse = userService.updateUserRole(userId, userRoleUpdateRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PostMapping("/{userId}/update_photo")
  public ResponseEntity<UserResponse> updatePhoto(
      @PathVariable Long userId, @Valid @RequestBody MultipartFile file) {
    UserResponse userResponse = userService.updatePhoto(userId, file);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PutMapping("/{userId}/change_password")
  public ResponseEntity<UserResponse> updateUserPassword(
      @PathVariable Long userId, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    UserResponse userResponse = userService.resetUserPassword(userId, resetPasswordRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
    UserResponse userResponse = userService.findUserById(userId);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @GetMapping("/currentUser")
  public ResponseEntity<UserResponse> getCurrentUser() {
    UserResponse userResponse = userService.getCurrentUser();
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long userId) {
    userService.deleteUserById(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
