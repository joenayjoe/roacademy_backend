package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Grade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {

  // @Query(value = "SELECT * FROM grade g where g.category_id = ?1", nativeQuery = true)
  @Query("select g from Grade g join g.category c where c.id = :categoryId")
  Iterable<Grade> findAllByCategoryId(Long categoryId);
}
