package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoleService {

  Iterable<Role> getAllRole();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Role createRole(Role role);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Role updateRole(Long roleId, Role role);

  Role getRoleById(Long roleId);

  Role getRoleByName(RoleEnum name);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteRoleById(Long roleId);
}
