package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.youtube.YoutubeMetaData;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileUploadService {

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  String uploadFile(String resourceClassName, Long resourceId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  String uploadToYoutube(YoutubeMetaData metaData, MultipartFile file);

  Resource getFileAsResource(String fileName);

  void deleteFileOrDirectory(String uri);
}
