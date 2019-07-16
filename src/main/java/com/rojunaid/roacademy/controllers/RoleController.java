package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

  @Autowired RoleService roleService;

  @GetMapping("")
  public ResponseEntity<Iterable<Role>> getAllRoles() {
    Iterable<Role> roles = roleService.getAllRole();
    return new ResponseEntity<>(roles, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
    Role persistentRole = roleService.createRole(role);
    return new ResponseEntity<>(persistentRole, HttpStatus.CREATED);
  }

  @PutMapping("/{roleId}")
  public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @Valid @RequestBody Role role) {
    Role updatedRole = roleService.updateRole(roleId, role);
    return new ResponseEntity<>(updatedRole, HttpStatus.OK);
  }

  @GetMapping("/{roleId}")
  public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
    Role role = roleService.getRoleById(roleId);
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  @DeleteMapping("/roleId")
  public ResponseEntity<HttpStatus> deleteRoleById(@PathVariable Long roleId) {
    roleService.deleteRoleById(roleId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
