package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Role;

public interface RoleService {

  Iterable<Role> getAllRole();

  Role createRole(Role role);

  Role updateRole(Long roleId, Role role);

  Role getRoleById(Long roleId);

  void deleteRoleById(Long roleId);
}
