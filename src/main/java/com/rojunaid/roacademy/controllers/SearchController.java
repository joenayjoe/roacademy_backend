package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.SearchResponse;
import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.models.CourseStatusEnum;
import com.rojunaid.roacademy.services.SearchService;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

  @Autowired private SearchService searchService;
  @Autowired private TagService tagService;

  @GetMapping("")
  public ResponseEntity<?> search(
      @RequestParam String query,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE) int page,
      @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(defaultValue = Constants.DEFAULT_SORTING) String order,
      @RequestParam(defaultValue = Constants.DEFAULT_COURSE_STATUS) List<CourseStatusEnum> status) {
    Page<SearchResponse> searchResponses = searchService.search(query, page, size, order, status);

    return new ResponseEntity<>(searchResponses, HttpStatus.OK);
  }

  // For tag search

  @GetMapping("/tags")
  ResponseEntity<List<TagResponse>> searchTags(@RequestParam String name) {
    List<TagResponse> tagResponses = tagService.search(name);
    return new ResponseEntity<>(tagResponses, HttpStatus.OK);
  }
}
