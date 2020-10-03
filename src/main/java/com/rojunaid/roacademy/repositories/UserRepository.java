package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @Query(
      value =
          "select crs from User instr left join instr.teachingCourses crs left join fetch crs.category left join fetch crs.grade left join fetch crs.createdBy creator left join fetch creator.roles where instr.id = ?1 and crs.status in (?2)",
      countQuery =
          "select count (crs) from User instr left join instr.teachingCourses crs where instr.id = ?1 and crs.status in (?2)")
  Page<Course> findTeachingCourses(
      Long instructorId, List<CourseStatusEnum> status, Pageable pageable);

  @Query(
      value =
          "select crs from User std left join std.enrolledCourses crs left join fetch crs.category left join fetch crs.grade left join fetch crs.createdBy creator left join fetch creator.roles where std.id = ?1",
      countQuery =
          "select count (crs) from User std left join std.enrolledCourses crs where std.id = ?1")
  Page<Course> findSubscribedCourses(Long userId, Pageable pageable);
}
