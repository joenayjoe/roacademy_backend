package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.ResetPasswordDTO;
import com.rojunaid.roacademy.dto.SignUpDTO;
import com.rojunaid.roacademy.dto.UserDTO;
import com.rojunaid.roacademy.models.User;

public interface UserService {

  User findByEmail(String email);

  User createUser(UserDTO userDTO);

  User registerNewUser(SignUpDTO signUpDTO);

  User updateUser(Long userId, UserDTO userDTO);

  User findUserById(Long userId);

  void deleteUserById(Long id);

  Iterable<User> findAll();

  User resetUserPassword(Long userId, ResetPasswordDTO resetPasswordDTO);
}
