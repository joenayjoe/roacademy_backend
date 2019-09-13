package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query(value = "select * from course where course.grade_id = :gradeId", nativeQuery = true)
  Iterable<Course> findAllByGradeId(Long gradeId);
}
