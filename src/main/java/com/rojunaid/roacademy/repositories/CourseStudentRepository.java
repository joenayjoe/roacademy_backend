package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStudent;
import com.rojunaid.roacademy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {

  @Query(
      value =
          "select crs from CourseStudent cstd left join cstd.course crs left join fetch crs.category left join fetch crs.grade left join fetch crs.createdBy creator left join fetch creator.roles where cstd.student.id = ?1",
      countQuery =
          "select count(crs) from CourseStudent cstd left join cstd.course crs where cstd.student.id = ?1")
  Page<Course> findSubscribedCourses(Long studentId, Pageable pageable);

  Optional<CourseStudent> findByStudentAndCourse(User student, Course course);
}
