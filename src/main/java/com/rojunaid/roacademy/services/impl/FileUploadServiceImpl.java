package com.rojunaid.roacademy.services.impl;

import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.google.api.services.youtube.model.Video;
import com.rojunaid.roacademy.auth.oauth2.UploadedResourceInfo;
import com.rojunaid.roacademy.auth.oauth2.box.BoxApiManager;
import com.rojunaid.roacademy.auth.oauth2.imgur.ImgurApiManager;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeApiManager;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeMetaData;
import com.rojunaid.roacademy.exception.DirectoryCreationException;
import com.rojunaid.roacademy.exception.MediaUploadException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  private static final String RESOURCE_FOLDER_NAME = "resources";

  @Autowired private YoutubeApiManager youtubeApiManager;
  @Autowired private BoxApiManager boxApiManager;
  @Autowired private ImgurApiManager imgurApiManager;

  @Override
  public String uploadToLocal(String resourceClassName, Long resourceId, MultipartFile file) {

    resourceClassName = resourceClassName.toLowerCase();
    String fileName = file.getOriginalFilename();

    // create a directory with resource type, eg: VideoResource
    Path resourceTypePath =
        Paths.get(uploadDir, RESOURCE_FOLDER_NAME, resourceClassName).normalize();
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
    Path fileUrl =
        Paths.get("/", RESOURCE_FOLDER_NAME, resourceClassName, resourceId.toString(), fileName)
            .normalize();
    return fileUrl.toString();
  }

  @Override
  public UploadedResourceInfo uploadToBox(
      String resourceType, Long resourceId, MultipartFile file) {

    String fileName = file.getOriginalFilename();
    BoxFolder.Info folder;
    BoxFolder.Info parentFolder = boxApiManager.getFolderByName(resourceType, "0");
    if (parentFolder == null) {
      parentFolder = boxApiManager.createFolder(resourceType, "0");
    }
    folder = boxApiManager.getFolderByName(resourceId.toString(), parentFolder.getID());
    if (folder == null) {
      folder = boxApiManager.createFolder(resourceId.toString(), parentFolder.getID());
    }

    boolean nameExist = false;
    BoxFile.Info savedFile = null;
    for (BoxItem.Info boxItemInfo : folder.getResource()) {
      if (boxItemInfo instanceof BoxFile.Info) {
        if (boxItemInfo.getName().equals(fileName)) {
          nameExist = true;
          savedFile = (BoxFile.Info) boxItemInfo;
          break;
        }
      }
    }
    if (nameExist) {
      // update file version
      try {
        BoxFile.Info updatedFile = savedFile.getResource().uploadNewVersion(file.getInputStream());
        UploadedResourceInfo resourceInfo = new UploadedResourceInfo();
        resourceInfo.setResourceId(updatedFile.getID());
        resourceInfo.setResourceUrl(boxApiManager.getSharedLink(updatedFile.getID()));
        return resourceInfo;
      } catch (IOException e) {
        throw new MediaUploadException(e.getLocalizedMessage());
      }

    } else {
      // upload new file
      try {
        BoxFile.Info newFileInfo = folder.getResource().uploadFile(file.getInputStream(), fileName);
        UploadedResourceInfo resourceInfo = new UploadedResourceInfo();
        resourceInfo.setResourceId(newFileInfo.getID());
        resourceInfo.setResourceUrl(boxApiManager.getSharedLink(newFileInfo.getID()));
        return resourceInfo;
      } catch (IOException e) {
        throw new MediaUploadException(e.getLocalizedMessage());
      }
    }
  }

  @Override
  public void deleteFromBox(String fileId) {
    boxApiManager.deleteFile(fileId);
  }

  @Override
  public UploadedResourceInfo uploadToImgur(MultipartFile file) {
    return imgurApiManager.uploadImage(file);
  }

  @Override
  public void deleteFromImgur(String imageId) {
    imgurApiManager.deleteImage(imageId);
  }

  @Override
  public UploadedResourceInfo uploadToYoutube(YoutubeMetaData metaData, MultipartFile file) {
    Video video = youtubeApiManager.upload(metaData, file);
    UploadedResourceInfo resourceInfo = new UploadedResourceInfo();
    resourceInfo.setResourceId(video.getId());
    resourceInfo.setResourceUrl(String.format("%s%s", YoutubeApiManager.BASE_URL, video.getId()));
    return resourceInfo;
  }

  @Override
  public void deleteFromYoutube(String resourceId) {
    youtubeApiManager.delete(resourceId);
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
