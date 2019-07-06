package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService {

  @Autowired GradeRepository gradeRepository;

  @Override
  public Iterable<Grade> getAllGrade() {
    return gradeRepository.findAll();
  }

  @Override
  public Grade createGrade(Grade grade) {
    return gradeRepository.save(grade);
  }

  @Override
  public Grade updateGrade(Long gradeId, Grade grade) {

    if (gradeRepository.existsById(gradeId)) {
      return gradeRepository.save(grade);
    }
    throw this.gradeNotFoundException(gradeId);
  }

  @Override
  public Grade findGradeById(Long gradeId) {
    return gradeRepository
        .findById(gradeId)
        .orElseThrow(() -> this.gradeNotFoundException(gradeId));
  }

  @Override
  public void deleteGradeById(Long gradeId) {
    if (gradeRepository.existsById(gradeId)) {
      gradeRepository.deleteById(gradeId);
    } else {
      throw this.gradeNotFoundException(gradeId);
    }
  }

  // private methods

  private ResourceNotFoundException gradeNotFoundException(Long gradeId) {
    return new ResourceNotFoundException("Grade with id " + gradeId + " not found");
  }

}
