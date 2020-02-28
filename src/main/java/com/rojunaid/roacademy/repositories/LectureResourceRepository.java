package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.LectureResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureResourceRepository extends JpaRepository<LectureResource, Long> {
}
