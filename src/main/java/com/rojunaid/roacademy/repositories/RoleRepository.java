package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Role;
import com.rojunaid.roacademy.models.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByName(RoleEnum name);
}
