package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.SignUpDTO;
import com.rojunaid.roacademy.dto.UserResponse;
import com.rojunaid.roacademy.dto.UserRoleUpdateDTO;
import com.rojunaid.roacademy.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User findByEmail(String email);

  //  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  //  UserResponse createUser(UserDTO userDTO);

  UserResponse registerNewUser(SignUpDTO signUpDTO);

  //  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  //  UserResponse updateUser(Long userId, UserUpdateDTO userUpdateDTO);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateUserRole(Long userId, UserRoleUpdateDTO userRoleUpdateDTO);

  @PreAuthorize("#userId == authentication.principal.user.id")
  UserResponse updatePhoto(Long userId, MultipartFile file);

  UserResponse findUserById(Long userId);

  UserResponse getCurrentUser();

  @PreAuthorize("#email == authentication.principal.user.email or hasRole('ADMIN')")
  void deleteUserById(Long id);

  @PreAuthorize("hasRole('ADMIN')")
  Iterable<UserResponse> findAll();

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  UserResponse resetUserPassword(Long userId, ResetPasswordDTO resetPasswordDTO);

  UserResponse userToUserResponse(User user);
}
