package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.SearchResponse;
import com.rojunaid.roacademy.models.Course;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.SearchService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

  @Autowired private CourseService courseService;

  @Override
  public Page<SearchResponse> search(
      String query, int page, int size, String order, List<CourseStatusEnum> status) {

    return courseService.search(query, page, size, order, status);
  }
}
