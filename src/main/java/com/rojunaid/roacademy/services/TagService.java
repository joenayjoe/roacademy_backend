package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.models.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

public interface TagService {

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Tag findOrCreateByName(String name);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Set<Tag> findOrCreateByNames(List<String> names);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  TagResponse tagToTagResponse(Tag tag);

  List<TagResponse> search(String name);
}
