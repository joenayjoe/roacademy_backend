package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query("SELECT crs FROM Course  crs WHERE crs.status in (?1)")
  Page<Course> findAll(List<CourseStatusEnum> status, Pageable page);

  @Query("select crs from Course  crs where crs.category.id = ?1 and crs.status in (?2)")
  Page<Course> findAllByCategoryId(Long categoryId, List<CourseStatusEnum> status, Pageable page);

  @Query("select crs from Course  crs where crs.grade.id = ?1 and crs.status in (?2)")
  Page<Course> findAllByGradeId(Long gradeId, List<CourseStatusEnum> status, Pageable page);

  @Query("select crs from Course  crs where crs.grade.id = ?1 and crs.status in (?2)")
  Iterable<Course> findAllByGradeId(Long gradeId, List<CourseStatusEnum> status, Sort sort);

  @Query("SELECT crs from Course crs where crs.name LIKE %?1%  and crs.status in (?2)")
  Page<Course> search(String query, List<CourseStatusEnum> status, Pageable page);

  @Query(
      "SELECT crs FROM Course  crs left join fetch crs.courseRequirements left join fetch crs.objectives left  join fetch crs.grade left  join  fetch crs.category left join fetch crs.createdBy where crs.id = ?1 and crs.status in (?2)")
  Optional<Course> findById(Long courseId, List<CourseStatusEnum> status);
}
