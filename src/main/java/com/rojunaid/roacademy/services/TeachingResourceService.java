package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.models.TeachingResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeachingResourceService {

  Iterable<TeachingResourceResponse> getTeachingResourceByChapter(Long chapterId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  TeachingResourceResponse uploadTeachingResource(TeachingResourceDTO teachingResourceDTO, MultipartFile file);
}
