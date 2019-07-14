package com.rojunaid.roacademy.validator.impl;

import com.rojunaid.roacademy.validator.FieldMatch;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

  private String firstFieldName;
  private String secondFieldName;
  private String message;

  @Override
  public void initialize(FieldMatch constrainAnnotation) {
    firstFieldName = constrainAnnotation.first();
    secondFieldName = constrainAnnotation.second();
    message = constrainAnnotation.message();
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean valid = true;
    try {
      final Object firstObj = BeanUtils.getProperty(object, firstFieldName);
      final Object secondObj = BeanUtils.getProperty(object, secondFieldName);
      valid =
          firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
    } catch (final Exception ignore) {
      // ignore
    }

    if (!valid) {
      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(secondFieldName)
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
    }

    return valid;
  }
}
