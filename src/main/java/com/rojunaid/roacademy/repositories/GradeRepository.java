package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Grade;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

  // @Query(value = "SELECT * FROM grade g where g.category_id = ?1", nativeQuery = true)
  @Query("select g from Grade g join g.category c where c.id = :categoryId")
  Iterable<Grade> findAllByCategoryId(Long categoryId, Sort sort);
}
