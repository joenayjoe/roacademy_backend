package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  Optional<Tag> findByName(String name);

  @Query(value = "SELECT * FROM tag WHERE name LIKE ?1% LIMIT 10", nativeQuery = true)
  Iterable<Tag> search(String name);
}
