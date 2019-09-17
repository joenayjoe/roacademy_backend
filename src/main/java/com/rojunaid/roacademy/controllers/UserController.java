package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.UserDTO;
import com.rojunaid.roacademy.dto.UserResponse;
import com.rojunaid.roacademy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("")
  public ResponseEntity<Iterable<UserResponse>> getAllUsers() {
    Iterable<UserResponse> userResponses = userService.findAll();
    return new ResponseEntity<>(userResponses, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDTO userDTO) {
    UserResponse userResponse = userService.createUser(userDTO);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
    UserResponse userResponse = userService.updateUser(userId, userDTO);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PutMapping("/{userId}/change_password")
  public ResponseEntity<UserResponse> updateUserPassword(
      @PathVariable Long userId, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
    UserResponse userResponse = userService.resetUserPassword(userId, resetPasswordDTO);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
    UserResponse userResponse = userService.findUserById(userId);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long userId) {
    userService.deleteUserById(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
