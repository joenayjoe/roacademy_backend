package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.repositories.TagRepository;
import com.rojunaid.roacademy.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  @Override
  public Set<Tag> findOrCreateByNames(List<String> tag_names) {
    Set<Tag> tags = new HashSet<>();
    for (String name : tag_names) {
      tags.add(this.findOrCreateByName(name.trim()));
    }
    return tags;
  }

  @Override
  public List<TagResponse> search(String name) {
    Iterable<Tag> tags = tagRepository.search(name);

    List<TagResponse> responses = new ArrayList<>();
    for (Tag tag : tags) {
      responses.add(tagToTagResponse(tag));
    }
    return responses;
  }

  @Override
  public TagResponse tagToTagResponse(Tag tag) {
    TagResponse tagResponse = new TagResponse();
    tagResponse.setId(tag.getId());
    tagResponse.setName(tag.getName());
    return tagResponse;
  }
}
