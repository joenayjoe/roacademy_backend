package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.RoleResponse;
import com.rojunaid.roacademy.models.Role;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoleService {

  Iterable<RoleResponse> getAllRole();

  @PreAuthorize("hasRole('ADMIN')")
  RoleResponse createRole(Role role);

  @PreAuthorize("hasRole('ADMIN')")
  RoleResponse updateRole(Long roleId, Role role);

  RoleResponse getRoleById(Long roleId);

  @PreAuthorize("hasRole('ADMIN')")
  void deleteRoleById(Long roleId);

  Role findOrCreateStudentRole();

  RoleResponse roleToRoleResponse(Role role);
}
