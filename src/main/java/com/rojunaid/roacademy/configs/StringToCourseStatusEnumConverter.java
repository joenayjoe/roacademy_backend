package com.rojunaid.roacademy.configs;

import com.rojunaid.roacademy.models.CourseStatusEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCourseStatusEnumConverter implements Converter<String, CourseStatusEnum> {

  @Override
  public CourseStatusEnum convert(String source) {
    try{
      return CourseStatusEnum.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
