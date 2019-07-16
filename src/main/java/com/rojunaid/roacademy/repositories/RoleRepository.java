package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {}
