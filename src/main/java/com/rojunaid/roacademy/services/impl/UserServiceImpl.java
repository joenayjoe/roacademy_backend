package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.BadRequestException;
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
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  public UserResponse updateUser(Long userId, UserUpdateRequest request) {
    User user = getUser(userId);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    List<Role> roles = roleRepository.findAllById(request.getRoleIds());
    Set<Role> roleSet = roles.stream().collect(Collectors.toSet());
    user.setRoles(roleSet);
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse updateUserRole(Long userId, UserRoleUpdateRequest userRoleUpdateRequest) {
    User user = this.getUser(userId);
    List<Role> roles = roleRepository.findAllById(userRoleUpdateRequest.getRoleIds());
    Set<Role> roleSet = roles.stream().collect(Collectors.toSet());
    user.setRoles(roleSet);
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse updatePhoto(Long userId, MultipartFile file) {
    User user = getUser(userId);
    String userClassName = User.class.getSimpleName();
    String imageUrl = this.fileUploadService.uploadFile(userClassName, userId, file);
    user.setImageUrl(imageUrl);
    user = userRepository.save(user);
    return this.userToUserResponse(user);
  }

  @Override
  public UserResponse findUserById(Long userId) {
    User user = getUser(userId);
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
  @Transactional
  public void deleteUserById(Long userId) {
    User user = this.getUser(userId);
    userRepository.delete(user);
    String uri = User.class.getSimpleName() + "/" + user.getId().toString();
    fileUploadService.deleteFileOrDirectory(uri);
  }

  @Override
  public Page<UserResponse> findAll(int page, int size, String sorting) {
    PageRequest pageable = PageRequest.of(page, size, SortingUtils.SortBy(sorting));
    Page<User> userPage = userRepository.findAll(pageable);
    Page<UserResponse> userResponses = userPage.map(u -> userToUserResponse(u));
    return userResponses;
  }

  @Override
  public UserResponse updateEmail(Long userId, ResetEmailRequest emailRequest) {
    User user = getUser(userId);
    user.setEmail(emailRequest.getEmail());
    user = userRepository.save(user);
    return userToUserResponse(user);
  }

  @Override
  public UserResponse resetUserPassword(Long userId, ResetPasswordRequest resetPasswordRequest) {
    User user = getUser(userId);
    boolean match =
        passwordEncoder.matches(resetPasswordRequest.getOldPassword(), user.getHashPassword());
    if (match) {
      user.setHashPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
      user = userRepository.save(user);
      return this.userToUserResponse(user);
    } else {
      throw new BadRequestException(Translator.toLocale("Credential.mismatch"));
    }
  }

  @Override
  public UserResponse userToUserResponse(User user) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getLastName());
    userResponse.setEmail(user.getEmail());
    userResponse.setCreatedAt(user.getCreatedAt());
    userResponse.setUpdatedAt(user.getUpdatedAt());

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

  User getUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> this.userNotFoundException(userId));
  }

  ResourceNotFoundException userNotFoundException(Long userId) {
    return new ResourceNotFoundException(
        Translator.toLocale("User.id.notfound", new Object[] {userId}));
  }
}
