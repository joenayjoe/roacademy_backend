package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.RoleResponse;
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
  public ResponseEntity<Iterable<RoleResponse>> getAllRoles() {
    Iterable<RoleResponse> roleResponses = roleService.getAllRole();
    return new ResponseEntity<>(roleResponses, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody Role role) {
    RoleResponse roleResponse = roleService.createRole(role);
    return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{roleId}")
  public ResponseEntity<RoleResponse> updateRole(@PathVariable Long roleId, @Valid @RequestBody Role role) {
    RoleResponse roleResponse = roleService.updateRole(roleId, role);
    return new ResponseEntity<>(roleResponse, HttpStatus.OK);
  }

  @GetMapping("/{roleId}")
  public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long roleId) {
    RoleResponse roleResponse = roleService.getRoleById(roleId);
    return new ResponseEntity<>(roleResponse, HttpStatus.OK);
  }

  @DeleteMapping("/roleId")
  public ResponseEntity<HttpStatus> deleteRoleById(@PathVariable Long roleId) {
    roleService.deleteRoleById(roleId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
