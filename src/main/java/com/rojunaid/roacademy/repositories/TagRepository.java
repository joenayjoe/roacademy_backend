package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
  Optional<Tag> findByName(String name);
}
