package com.rojunaid.roacademy;

import com.rojunaid.roacademy.auth.oauth2.OAuth2CredentialService;
import com.rojunaid.roacademy.configs.AppProperties;
import com.rojunaid.roacademy.models.AuthProvider;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.RoleRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class RoacademyApplication implements CommandLineRunner {

  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private Environment environment;
  @Autowired private OAuth2CredentialService oAuth2CredentialService;

  public static void main(String[] args) {
    SpringApplication.run(RoacademyApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    // create ADMIN role and ADMIN user
    Role role = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElse(null);
    if (role == null) {
      role = new Role();
      role.setName(RoleEnum.ROLE_ADMIN);
      role = roleRepository.save(role);
    }

    Role teacherRole = roleRepository.findByName(RoleEnum.ROLE_TEACHER).orElse(null);
    if (teacherRole == null) {
      teacherRole = new Role();
      teacherRole.setName(RoleEnum.ROLE_TEACHER);
      roleRepository.save(teacherRole);
    }

    Role studentRole = roleRepository.findByName(RoleEnum.ROLE_STUDENT).orElse(null);
    if (studentRole == null) {
      studentRole = new Role();
      studentRole.setName(RoleEnum.ROLE_STUDENT);
      roleRepository.save(studentRole);
    }

    Iterable<User> users = userRepository.findAll();
    if (!users.iterator().hasNext()) {
      User user = new User();
      user.setFirstName("Admin");
      user.setLastName("Admin");
      user.setEmail("admin@example.com");
      user.setHashPassword(passwordEncoder.encode("password"));
      user.setEnable(true);
      user.setRoles(new HashSet<>(Arrays.asList(role)));
      user.setProvider(AuthProvider.local);
      userRepository.save(user);
    }

    // create upload directory if does not exist
    String path = environment.getProperty("file.upload-dir");
    File uploadDir = new File(path);
    // create upload directory if does not exist
    if (!uploadDir.exists()) {
      if (uploadDir.mkdirs()) {
        System.out.println("Directory " + path + " created.");
      } else {
        System.out.println("Directory " + path + " creation failed.");
      }
    }
  }
}
