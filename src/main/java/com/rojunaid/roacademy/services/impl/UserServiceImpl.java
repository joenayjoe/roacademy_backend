package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.SignUpDTO;
import com.rojunaid.roacademy.dto.UserDTO;
import com.rojunaid.roacademy.exception.ResourceAlreadyExistException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.RoleRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import com.rojunaid.roacademy.services.UserService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

  @Autowired UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired RoleRepository roleRepository;

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
  public User createUser(UserDTO userDTO) {
    User existedUser = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
    if (existedUser != null) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("User.email.exist", new Object[] {userDTO.getEmail()}));
    }
    User user = this.userDTOToUser(userDTO);
    return userRepository.save(user);
  }

  // does not need to pre authorize
  @Override
  public User registerNewUser(SignUpDTO signUpDTO) {
    User existedUser = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);
    if (existedUser != null) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("User.email.exist", new Object[] {signUpDTO.getEmail()}));
    }
    User user = new User();
    user.setFirstName(signUpDTO.getFirstName());
    user.setLastName(signUpDTO.getLastName());
    user.setEmail(signUpDTO.getEmail());
    user.setHashPassword(passwordEncoder.encode(signUpDTO.getPassword()));

    Role role = roleRepository.findById(signUpDTO.getRoleId()).orElse(null);
    if (role == null) {
      role = this.findOrCreateStudentRole();
    }
    if (role.getName() == RoleEnum.ROLE_TEACHER || role.getName() == RoleEnum.ROLE_ADMIN) {
      user.setEnable(false);
    }
    return userRepository.save(user);
  }

  @Override
  public User updateUser(Long userId, UserDTO userDTO) {
    User oldUser = this.findUserById(userId);
    oldUser.setFirstName(userDTO.getFirstName());
    oldUser.setLastName(userDTO.getLastName());
    oldUser.setRoles(this.getRoles(userDTO.getRoleIds()));
    return userRepository.save(oldUser);
  }

  @Override
  public User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> this.userNotFoundException(userId));
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
  public Iterable<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User resetUserPassword(Long userId, ResetPasswordDTO resetPasswordDTO) {
    User user = this.findUserById(userId);
    user.setHashPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
    return userRepository.save(user);
  }

  // private method

  ResourceNotFoundException userNotFoundException(Long userId) {
    return new ResourceNotFoundException(Translator.toLocale("User.id.notfound", new Object[]{userId}));
  }

  private User userDTOToUser(UserDTO userDTO) {
    User user = new User();
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setEmail(userDTO.getEmail());
    user.setHashPassword(passwordEncoder.encode(userDTO.getPassword()));
    user.setRoles(this.getRoles(userDTO.getRoleIds()));
    return user;
  }

  private Set<Role> getRoles(List<Long> roleIds) {
    Set<Role> roles = new HashSet<>();
    List<Long> notFoundIds = new ArrayList<>();
    for (Long roleId : roleIds) {
      Role role = roleRepository.findById(roleId).orElse(null);
      if (role == null) {
        notFoundIds.add(roleId);
      } else {
        roles.add(role);
      }
    }
    if (notFoundIds.size() > 0) {
      throw new ResourceNotFoundException(Translator.toLocale("Role.id.notfound", notFoundIds.toArray()));
    }
    return roles;
  }

  private Role findOrCreateStudentRole() {
    Role role = roleRepository.findByName(RoleEnum.ROLE_STUDENT).orElse(null);
    if (role != null) {
      return role;
    }
    role = new Role();
    role.setName(RoleEnum.ROLE_STUDENT);
    return roleRepository.save(role);
  }
}
