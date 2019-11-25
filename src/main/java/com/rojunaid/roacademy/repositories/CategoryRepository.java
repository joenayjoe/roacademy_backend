package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("select distinct category from Category category left outer join fetch  category.grades")
  List<Category> findAllWithGrades();

  @Query(
      "select category from Category category left join fetch category.grades where category.id=:categoryId")
  Optional<Category> finCategoryWithGradesById(@Param("categoryId") Long categoryId);

  @Query(
      "select category, grade, course from Category category left join fetch category.grades grade left join fetch grade.courses course where  category.id=:categoryId")
  Optional<Category> finCategoryWithGradesAndCoursesById(@Param("categoryId") Long categoryId);
}
