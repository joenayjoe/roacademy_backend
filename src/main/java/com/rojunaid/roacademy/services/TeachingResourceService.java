package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.models.TeachingResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeachingResourceService {

  TeachingResource getTeachingResourceById(Long id);

  TeachingResource getTeachingResourceByName(String name);

  Iterable<TeachingResource> getTeachingResourceByTag(String tag);

  Iterable<TeachingResource> getTeachingResourceByTags(List<String> tags);

  Iterable<TeachingResource> getTeachingResourceByChapter(Long chapterId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  TeachingResource uploadTeachingResource(TeachingResourceDTO teachingResourceDTO, MultipartFile file);
}
