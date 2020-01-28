package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
