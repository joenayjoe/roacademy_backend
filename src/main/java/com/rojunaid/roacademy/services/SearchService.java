package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.SearchResponse;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

  Page<SearchResponse> search(
      String query, int page, int size, String order, List<CourseStatusEnum> status);
}
