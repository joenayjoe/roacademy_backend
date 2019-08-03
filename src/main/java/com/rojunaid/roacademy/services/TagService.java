package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Tag;

import java.util.Set;

public interface TagService {

  Tag findOrCreateByName(String name);
  Set<Tag> findOrCreateByName(Set<String> names);
}
