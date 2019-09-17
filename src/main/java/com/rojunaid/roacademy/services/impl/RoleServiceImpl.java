package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.RoleResponse;
import com.rojunaid.roacademy.exception.ResourceAlreadyExistException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.repositories.RoleRepository;
import com.rojunaid.roacademy.services.RoleService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired RoleRepository roleRepository;

  @Override
  public Iterable<RoleResponse> getAllRole() {
    Iterable<Role> roles = roleRepository.findAll();
    List<RoleResponse> roleResponses = new ArrayList<>();
    for (Role role : roles) {
      roleResponses.add(this.roleToRoleResponse(role));
    }
    return roleResponses;
  }

  @Override
  public RoleResponse createRole(Role role) {

    try {
      role = roleRepository.save(role);
      return this.roleToRoleResponse(role);
    } catch (DataIntegrityViolationException exp) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("Role.name.exist", new Object[] {role.getName()}));
    }
  }

  @Override
  public RoleResponse updateRole(Long roleId, Role role) {
    Role existingRole =
        roleRepository.findById(roleId).orElseThrow(() -> this.notFoundException(roleId));
    existingRole.setName(role.getName());
    try {
      role = roleRepository.save(existingRole);
      return this.roleToRoleResponse(role);
    } catch (DataIntegrityViolationException exp) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("Role.name.exist", new Object[] {role.getName()}));
    }
  }

  @Override
  public RoleResponse getRoleById(Long roleId) {
    Role role = roleRepository.findById(roleId).orElseThrow(() -> this.notFoundException(roleId));
    return this.roleToRoleResponse(role);
  }

  @Override
  public void deleteRoleById(Long roleId) {
    if (roleRepository.existsById(roleId)) {
      roleRepository.deleteById(roleId);
    } else {
      throw this.notFoundException(roleId);
    }
  }

  @Override
  public RoleResponse roleToRoleResponse(Role role) {
    RoleResponse roleResponse = new RoleResponse();
    roleResponse.setId(role.getId());
    roleResponse.setName(role.getName());
    return roleResponse;
  }

  // private methods

  private ResourceNotFoundException notFoundException(Long roleId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Role.id.notfound", new Object[] {roleId}));
  }
}
