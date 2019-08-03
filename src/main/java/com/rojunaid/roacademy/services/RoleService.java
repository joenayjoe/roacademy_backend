package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;

public interface RoleService {

  Iterable<Role> getAllRole();

  Role createRole(Role role);

  Role updateRole(Long roleId, Role role);

  Role getRoleById(Long roleId);

  Role getRoleByName(RoleEnum name);

  void deleteRoleById(Long roleId);
}
