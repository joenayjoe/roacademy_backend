package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

  // @Query(value = "SELECT * FROM grade g where g.category_id = ?1", nativeQuery = true)
  @Query("select g from Grade g join g.category c where c.id = :categoryId ORDER BY g.name ASC")
  Iterable<Grade> findAllByCategoryId(Long categoryId);

  @Query("select g from Grade g left join fetch g.courses where g.id=:gradeId ORDER BY g.name ASC")
  Optional<Grade> findGradeWithCoursesById(Long gradeId);
}
