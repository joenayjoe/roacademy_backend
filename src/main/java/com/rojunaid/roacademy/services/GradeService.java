package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Grade;

public interface GradeService {

    Iterable<Grade> getAllGrade();

    Grade createGrade(Grade grade);

    Grade updateGrade(Long gradeId, Grade grade);

    Grade findGradeById(Long gradeId);

    void deleteGradeById(Long gradeId);
}
