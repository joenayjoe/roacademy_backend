package com.rojunaid.roacademy.services.impl;

import com.google.api.services.youtube.model.Video;
import com.rojunaid.roacademy.exception.DirectoryCreationException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.util.Translator;
import com.rojunaid.roacademy.youtube.YoutubeMetaData;
import com.rojunaid.roacademy.youtube.YoutubeUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  private static final String resourceFolder = "resources";

  @Autowired private YoutubeUploader youtubeUploader;

  @Override
  public String uploadFile(String resourceClassName, Long resourceId, MultipartFile file) {

    resourceClassName = resourceClassName.toLowerCase();
    String fileName = file.getOriginalFilename();

    // create a directory with resource type, eg: LectureResource
    Path resourceTypePath = Paths.get(uploadDir, resourceFolder, resourceClassName).normalize();
    try {
      Files.createDirectories(resourceTypePath);
    } catch (Exception exp) {
      throw new DirectoryCreationException(Translator.toLocale("Directory.notcreated"));
    }

    // create a directory with resource id
    Path resourceIdPath = resourceTypePath.resolve(resourceId.toString());
    try {
      if (Files.exists(resourceIdPath)) {
        Files.walk(resourceIdPath)
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .forEach(File::delete);
      }
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
    Path fileUrl = Paths.get("/", resourceFolder, resourceClassName, resourceId.toString(), fileName).normalize();
    return fileUrl.toString();
  }

  @Override
  public String uploadToYoutube(YoutubeMetaData metaData, MultipartFile file) {
    Video video = youtubeUploader.upload(metaData, file);
    return video.getId();
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

  @Override
  public void deleteFileOrDirectory(String uri) {
    Path path = Paths.get(uploadDir, uri).normalize();
    if (Files.exists(path)) {
      try {
        Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
        Files.delete(path);
      } catch (Exception ex) {

      }
    }
  }
}
