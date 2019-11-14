package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.ResourceAlreadyExistException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.RoleRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import com.rojunaid.roacademy.security.AuthenticationFacade;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.RoleService;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.Helper;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired RoleRepository roleRepository;
  @Autowired RoleService roleService;
  @Autowired AuthenticationFacade authenticationFacade;
  @Autowired FileUploadService fileUploadService;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Override
  public User findByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("User.email.notfound", new Object[] {email})));
  }

  @Override
  public UserResponse registerNewUser(SignUpRequest signUpRequest) {
    User existedUser = userRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
    if (existedUser != null) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("User.email.exist", new Object[] {signUpRequest.getEmail()}));
    }
    User user = new User();
    user.setFirstName(signUpRequest.getFirstName());
    user.setLastName(signUpRequest.getLastName());
    user.setEmail(signUpRequest.getEmail());
    user.setHashPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    Role role = this.roleService.findOrCreateStudentRole();
    Set<Role> roleSet = new HashSet<>();
    roleSet.add(role);
    user.setRoles(roleSet);
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse updateUserRole(Long userId, UserRoleUpdateRequest userRoleUpdateRequest) {
    User user = userRepository.findById(userRoleUpdateRequest.getUserId()).orElse(null);
    if (user == null) {
      throw this.userNotFoundException(userId);
    }
    List<Role> roles = roleRepository.findAllById(userRoleUpdateRequest.getRoleIds());
    Set<Role> roleSet = roles.stream().collect(Collectors.toSet());
    user.setRoles(roleSet);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse updatePhoto(Long userId, MultipartFile file) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw this.userNotFoundException(userId);
    }
    String userClassName = User.class.getSimpleName();
    String imageUrl = this.fileUploadService.uploadFile(userClassName, userId, file);
    user.setImageUrl(imageUrl);
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse findUserById(Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> this.userNotFoundException(userId));
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse getCurrentUser() {
    CustomUserPrincipal principal =
        (CustomUserPrincipal) authenticationFacade.getAuthentication().getPrincipal();
    User user = principal.getUser();
    return this.userToUserResponse(user);
  }

  @Override
  public void deleteUserById(Long userId) {
    if (userRepository.existsById(userId)) {
      userRepository.deleteById(userId);
    } else {
      throw this.userNotFoundException(userId);
    }
  }

  @Override
  public Iterable<UserResponse> findAll() {
    Iterable<User> users = userRepository.findAll();
    List<UserResponse> userResponses = new ArrayList<>();
    for (User user : users) {
      userResponses.add(this.userToUserResponse(user));
    }
    return userResponses;
  }

  @Override
  public UserResponse resetUserPassword(Long userId, ResetPasswordRequest resetPasswordRequest) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw this.userNotFoundException(userId);
    }
    user.setHashPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse userToUserResponse(User user) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getLastName());
    userResponse.setEmail(user.getEmail());

    Set<Role> roles = user.getRoles();
    List<RoleResponse> roleResponses = new ArrayList<>();
    for (Role role : roles) {
      roleResponses.add(roleService.roleToRoleResponse(role));
    }

    userResponse.setRoles(roleResponses);
    if (user.getImageUrl() != null && !user.getImageUrl().startsWith("http")) {
      userResponse.setImageUrl(Helper.getBaseUrl() + user.getImageUrl());
    } else {
      userResponse.setImageUrl(user.getImageUrl());
    }
    return userResponse;
  }

  // private method

  ResourceNotFoundException userNotFoundException(Long userId) {
    return new ResourceNotFoundException(
        Translator.toLocale("User.id.notfound", new Object[] {userId}));
  }
}
