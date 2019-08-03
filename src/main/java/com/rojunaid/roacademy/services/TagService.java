package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Tag;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

public interface TagService {

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Tag findOrCreateByName(String name);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  Set<Tag> findOrCreateByName(Set<String> names);
}
