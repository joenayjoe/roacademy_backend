package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.SignUpDTO;
import com.rojunaid.roacademy.dto.UserDTO;
import com.rojunaid.roacademy.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

  User findByEmail(String email);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  User createUser(UserDTO userDTO);

  User registerNewUser(SignUpDTO signUpDTO);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  User updateUser(Long userId, UserDTO userDTO);

  User findUserById(Long userId);

  @PreAuthorize(
      "#email == authentication.principal.user.email or hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteUserById(Long id);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Iterable<User> findAll();

  @PreAuthorize(
      "#userId == authentication.principal.user.id or hasRole('ADMIN') or hasRole('TEACHER')")
  User resetUserPassword(Long userId, ResetPasswordDTO resetPasswordDTO);
}
