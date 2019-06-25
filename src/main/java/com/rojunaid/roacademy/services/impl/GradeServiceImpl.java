package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    GradeRepository gradeRepository;

    @Override
    public Iterable<Grade> getAllGrade() {
        return gradeRepository.findAll();
    }

    @Override
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Grade updateGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Optional<Grade> findById(Long gradeId) {
        return gradeRepository.findById(gradeId);
    }

    @Override
    public boolean isGradeExist(Long gradeId) {
        return gradeRepository.existsById(gradeId);
    }
}
