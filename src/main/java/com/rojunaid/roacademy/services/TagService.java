package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.models.Tag;

public interface TagService {

  Tag findOrCreateByName(String name);
}
