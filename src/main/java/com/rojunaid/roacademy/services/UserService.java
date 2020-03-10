package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User findByEmail(String email);

  //  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  //  UserResponse createUser(UserRequest userDTO);

  UserResponse registerNewUser(SignUpRequest signUpRequest);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse updateUser(Long userId, UserUpdateRequest userUpdateDTO);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateUserRole(Long userId, UserRoleUpdateRequest userRoleUpdateRequest);

  @PreAuthorize("#userId == authentication.principal.user.id")
  UserResponse updatePhoto(Long userId, MultipartFile file);

  UserResponse findUserById(Long userId);

  UserResponse getCurrentUser();

  @PreAuthorize("#email == authentication.principal.user.email or hasRole('ADMIN')")
  void deleteUserById(Long id);

  @PreAuthorize("hasRole('ADMIN')")
  Page<UserResponse> findAll(int page, int size, String sorting);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  UserResponse resetUserPassword(Long userId, ResetPasswordRequest resetPasswordRequest);

  UserResponse userToUserResponse(User user);
}
