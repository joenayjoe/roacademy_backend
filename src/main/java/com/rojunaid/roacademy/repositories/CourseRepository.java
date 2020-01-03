package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query(
      value =
          "select * from course where course.grade_id = :gradeId and course.status = 'PUBLISHED'",
      nativeQuery = true)
  Iterable<Course> findAllByGradeId(Long gradeId);

  @Query(
      "SELECT crs from Course crs where crs.name LIKE %?1% OR crs.description LIKE %?1% and crs.status='PUBLISHED'")
  Iterable<Course> search(String query);

  @Override
  @Query("SELECT distinct crs FROM Course  crs left join fetch crs.courseRequirements left join fetch crs.objectives")
  List<Course> findAll();
}
