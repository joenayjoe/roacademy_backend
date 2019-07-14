package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.GradeDTO;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Category;
import com.rojunaid.roacademy.models.Grade;
import com.rojunaid.roacademy.repositories.GradeRepository;
import com.rojunaid.roacademy.services.CategoryService;
import com.rojunaid.roacademy.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService {

  @Autowired GradeRepository gradeRepository;
  @Autowired CategoryService categoryService;

  @Override
  public Iterable<Grade> getAllGrade() {
    return gradeRepository.findAll();
  }

  @Override
  public Grade createGrade(Long categoryId, GradeDTO gradeDTO) {
    Category category = categoryService.findCategoryById(categoryId);
    Grade grade = this.gradeDTOToGrade(gradeDTO);
    grade.setCategory(category);
    return gradeRepository.save(grade);
  }

  @Override
  public Grade updateGrade(Long categoryId, Long gradeId, GradeDTO gradeDTO) {
    Category category = categoryService.findCategoryById(categoryId);
    if (gradeRepository.existsById(gradeId)) {
      Grade grade = this.gradeDTOToGrade(gradeDTO);
      grade.setCategory(category);
      grade.setId(gradeId);
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
  public Iterable<Grade> findGradesByCategoryId(Long categoryId) {
    return gradeRepository.findAllByCategoryId(categoryId);
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

  private Grade gradeDTOToGrade(GradeDTO gradeDTO) {
    Grade grade = new Grade();
    grade.setName(gradeDTO.getName());
    return grade;
  }
}
