package com.rojunaid.roacademy.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
  String uploadFile(String resourceClassName, Long resourceId, MultipartFile file);
  Resource getFileAsResource(String fileName);
}
