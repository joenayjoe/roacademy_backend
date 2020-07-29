package com.rojunaid.roacademy.services;

import com.rojunaid.roacademy.auth.oauth2.UploadedResourceInfo;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeMetaData;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  String uploadToLocal(String resourceClassName, Long resourceId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  UploadedResourceInfo uploadToBox(String resourceType, Long resourceId, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteFromBox(String url);

  UploadedResourceInfo uploadToImgur(MultipartFile file);

  void deleteFromImgur(String imageId);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  UploadedResourceInfo uploadToYoutube(YoutubeMetaData metaData, MultipartFile file);

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  void deleteFromYoutube(String resourceId);

  Resource getFileAsResource(String fileName);

  void deleteFileOrDirectory(String uri);
}
