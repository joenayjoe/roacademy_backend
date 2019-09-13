package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.RoleResponse;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoleService {

  Iterable<RoleResponse> getAllRole();

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  RoleResponse createRole(Role role);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  RoleResponse updateRole(Long roleId, Role role);

  RoleResponse getRoleById(Long roleId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteRoleById(Long roleId);

  RoleResponse roleToRoleResponse(Role role);
}
