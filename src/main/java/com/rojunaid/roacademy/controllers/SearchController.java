package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.CourseResponse;
import com.rojunaid.roacademy.dto.SearchRequest;
import com.rojunaid.roacademy.dto.SearchResponse;
import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.services.CourseService;
import com.rojunaid.roacademy.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

  @Autowired private CourseService courseService;
  @Autowired private TagService tagService;

  @GetMapping("")
  public ResponseEntity<?> search(@RequestParam String query) {
    Iterable<CourseResponse> courses = courseService.search(query);

    SearchResponse<CourseResponse> searchResponse = new SearchResponse<>();
    searchResponse.setEndPage(0L);
    searchResponse.setStarPage(0L);
    searchResponse.setTotalPage(1L);
    searchResponse.setResult(courses);
    return new ResponseEntity<>(searchResponse, HttpStatus.OK);
  }

  // For auto suggest
  @PostMapping("")
  ResponseEntity<Iterable<CourseResponse>> autoSuggest(@RequestBody SearchRequest request) {
    Iterable<CourseResponse> courseResponses = courseService.search(request.getQuery());
    return new ResponseEntity<>(courseResponses, HttpStatus.OK);
  }

  // For tag search

  @GetMapping("/tags")
  ResponseEntity<List<TagResponse>> searchTags(@RequestParam String name) {
    List<TagResponse> tagResponses =  tagService.search(name);
    return new ResponseEntity<>(tagResponses, HttpStatus.OK);
  }
}
