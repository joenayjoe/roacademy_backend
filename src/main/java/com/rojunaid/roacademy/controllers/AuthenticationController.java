package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.JwtAuthenticationResponseDTO;
import com.rojunaid.roacademy.dto.LoginDTO;
import com.rojunaid.roacademy.dto.SignUpDTO;
import com.rojunaid.roacademy.security.JwtTokenProvider;
import com.rojunaid.roacademy.services.RoleService;
import com.rojunaid.roacademy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserService userService;

  @Autowired RoleService roleService;

  @Autowired PasswordEncoder passwordEncoder;

  @Autowired JwtTokenProvider jwtTokenProvider;

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponseDTO> authenticateUser(
      @Valid @RequestBody LoginDTO loginDTO) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtTokenProvider.generateToken(authentication);
    JwtAuthenticationResponseDTO jwtResponse = new JwtAuthenticationResponseDTO();
    jwtResponse.setAccessToken(jwt);
    return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<HttpStatus> registerUer(@Valid @RequestBody SignUpDTO signUpDTO) {
    userService.registerNewUser(signUpDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
