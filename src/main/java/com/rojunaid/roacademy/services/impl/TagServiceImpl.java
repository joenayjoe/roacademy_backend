package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.repositories.TagRepository;
import com.rojunaid.roacademy.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

  @Autowired TagRepository tagRepository;

  @Override
  public Tag findOrCreateByName(String name) {
    Tag tag = new Tag();
    tag.setName(name);

    Tag existingTag = tagRepository.findByName(name).orElse(null);
    if (existingTag == null) {
      existingTag = tagRepository.save(tag);
    }
    return existingTag;
  }
}
