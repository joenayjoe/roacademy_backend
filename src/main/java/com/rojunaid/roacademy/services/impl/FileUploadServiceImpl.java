package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.exception.DirectoryCreationException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Override
  public String uploadFile(String resourceClassName, Long resourceId, MultipartFile file) {

    String fileName = file.getOriginalFilename();

    // create a directory with resource type, eg: TeachingResource
    Path resourceTypePath = Paths.get(uploadDir, resourceClassName).normalize();
    try {
      Files.createDirectories(resourceTypePath);
    } catch (Exception exp) {
      throw new DirectoryCreationException(Translator.toLocale("Directory.notcreated"));
    }

    // create a directory with database id of teaching resource
    Path resourceIdPath = resourceTypePath.resolve(resourceId.toString());
    try {
      Files.createDirectories(resourceIdPath);
    } catch (Exception exp) {
      throw new DirectoryCreationException(Translator.toLocale("Directory.notcreated"));
    }

    // copy file to target location
    Path targetLocation = resourceIdPath.resolve(fileName).normalize();
    try {
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception exp) {
      throw new DirectoryCreationException(Translator.toLocale("FileSave.error"));
    }
    Path fileUrl = Paths.get("/", resourceClassName, resourceId.toString(), fileName).normalize();
    return fileUrl.toString();
  }

  @Override
  public Resource getFileAsResource(String fileUrl) {
    try {
      Path filePath = Paths.get(uploadDir, fileUrl).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new ResourceNotFoundException(
            Translator.toLocale("File.notfound", new Object[] {fileUrl}));
      }
    } catch (MalformedURLException ex) {
      throw new ResourceNotFoundException(
          Translator.toLocale("File.notfound", new Object[] {fileUrl}));
    }
  }
}
