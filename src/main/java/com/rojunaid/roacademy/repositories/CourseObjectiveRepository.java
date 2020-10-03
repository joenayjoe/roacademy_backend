package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.CourseObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseObjectiveRepository extends JpaRepository<CourseObjective, Long> {}
