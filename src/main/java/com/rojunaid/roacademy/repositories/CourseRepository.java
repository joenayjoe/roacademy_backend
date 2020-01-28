package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query(
      value =
          "select * from course where course.category_id = ?1 and course.status in (?2) order by ?#{#sort}",
      nativeQuery = true)
  Iterable<Course> findAllByCategoryId(Long categoryId, List<String> status, Sort sort);

  @Query(
      value =
          "select * from course where course.grade_id = ?1 and course.status in (?2) order by ?#{#sort}",
      nativeQuery = true)
  Iterable<Course> findAllByGradeId(Long gradeId, List<String> status, Sort sort);

  @Query(
      "SELECT crs from Course crs where crs.name LIKE %?1% OR crs.description LIKE %?1% and crs.status='PUBLISHED'")
  Iterable<Course> search(String query);

  //  @Override
  //  @Query("SELECT distinct crs FROM Course  crs left join fetch crs.createdBy")
  //  List<Course> findAll();

  @Query(
      "SELECT crs FROM Course  crs left join fetch crs.courseRequirements left join fetch crs.objectives left  join fetch crs.grade left  join  fetch crs.category left join fetch crs.createdBy where crs.id = ?1")
  Optional<Course> findById(Long courseId);
}
