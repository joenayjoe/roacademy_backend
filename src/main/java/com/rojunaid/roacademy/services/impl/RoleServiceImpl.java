package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.exception.ResourceAlreadyExistException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import com.rojunaid.roacademy.repositories.RoleRepository;
import com.rojunaid.roacademy.services.RoleService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired RoleRepository roleRepository;

  @Override
  public Iterable<Role> getAllRole() {
    return roleRepository.findAll();
  }

  @Override
  public Role createRole(Role role) {

    try {
      return roleRepository.save(role);
    } catch (DataIntegrityViolationException exp) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("Role.name.exist", new Object[] {role.getName()}));
    }
  }

  @Override
  public Role updateRole(Long roleId, Role role) {
    Role existingRole = this.getRoleById(roleId);
    existingRole.setName(role.getName());
    try {
      return roleRepository.save(existingRole);
    } catch (DataIntegrityViolationException exp) {
      throw new ResourceAlreadyExistException(
          Translator.toLocale("Role.name.exist", new Object[] {role.getName()}));
    }
  }

  @Override
  public Role getRoleById(Long roleId) {
    return roleRepository.findById(roleId).orElseThrow(() -> this.notFoundException(roleId));
  }

  @Override
  public Role getRoleByName(RoleEnum name) {
    return roleRepository
        .findByName(name)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("Role.name.notfound", new Object[] {name.name()})));
  }

  @Override
  public void deleteRoleById(Long roleId) {
    if (roleRepository.existsById(roleId)) {
      roleRepository.deleteById(roleId);
    } else {
      throw this.notFoundException(roleId);
    }
  }

  // private methods

  private ResourceNotFoundException notFoundException(Long roleId) {
    return new ResourceNotFoundException(
        Translator.toLocale("Role.id.notfound", new Object[] {roleId}));
  }
}
