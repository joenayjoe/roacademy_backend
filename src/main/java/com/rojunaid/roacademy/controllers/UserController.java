package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.UserDTO;
import com.rojunaid.roacademy.models.User;
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
  public ResponseEntity<Iterable<User>> getAllUsers() {
    Iterable<User> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
    User newUser = userService.createUser(userDTO);
    return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<User> updateUser(
      @PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
    User updatedUser = userService.updateUser(userId, userDTO);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @PutMapping("/{userId}/change_password")
  public ResponseEntity<User> updateUserPassword(
      @PathVariable Long userId, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
    User user = userService.resetUserPassword(userId, resetPasswordDTO);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    User user = userService.findUserById(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long userId) {
    userService.deleteUserById(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
