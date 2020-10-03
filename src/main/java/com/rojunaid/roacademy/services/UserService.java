package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

  User findByEmail(String email);

  UserResponse registerNewUser(SignUpRequest signUpRequest);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateUser(Long userId, UserUpdateRequest userUpdateDTO);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateUserRole(Long userId, UserRoleUpdateRequest userRoleUpdateRequest);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  UserResponse updatePhoto(Long userId, MultipartFile file);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  UserResponse updateEmail(Long userId, ResetEmailRequest emailRequest);

  UserResponse findUserById(Long userId);

  UserResponse getCurrentUser();

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  void deleteUserById(Long userId);

  @PreAuthorize("hasRole('ADMIN')")
  Page<UserResponse> findAll(int page, int size, String sorting);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  UserResponse resetUserPassword(Long userId, ResetPasswordRequest resetPasswordRequest);

  void subscribeCourse(Long userId, CourseSubscriptionRequest subscription);

  CourseSubscriptionCheckResponse isSubscribed(Long userId, Long courseId);

  @PreAuthorize("#instructorId == authentication.principal.user.id or hasRole('ADMIN')")
  Page<CourseResponse> getTeachingCourses(
      Long instructorId, int page, int size, List<CourseStatusEnum> statusEnums, String order);

  @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
  Page<CourseResponse> getSubscribedCourses(Long studentId, int page, int size, String order);

  UserResponse userToUserResponse(User user);
}
