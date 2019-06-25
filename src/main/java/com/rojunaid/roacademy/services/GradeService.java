package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Grade;

import java.util.Optional;

public interface GradeService {

    Iterable<Grade> getAllGrade();

    Grade createGrade(Grade grade);

    Grade updateGrade(Grade grade);

    Optional<Grade> findGradeById(Long gradeId);

    boolean isGradeExist(Long gradeId);

    void deleteGradeById(Long gradeId);
}
