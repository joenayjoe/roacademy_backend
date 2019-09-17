package com.rojunaid.roacademy.services.impl;

import com.google.api.services.youtube.model.Video;
import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.exception.DirectoryCreationException;
import com.rojunaid.roacademy.exception.MediaTypeNotSupportedException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.models.TeachingResource;
import com.rojunaid.roacademy.repositories.TeachingResourceRepository;
import com.rojunaid.roacademy.security.AuthenticationFacade;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.services.TeachingResourceService;
import com.rojunaid.roacademy.util.Translator;
import com.rojunaid.roacademy.youtube.YoutubeUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TeachingResourceServiceImpl implements TeachingResourceService {

  private static final List<String> ALLOWED_MEDIA_TYPES =
      Arrays.asList("image/png", "image/jpeg", "video/", "application/pdf");
  @Autowired private TeachingResourceRepository teachingResourceRepository;
  @Autowired private TagService tagService;
  @Autowired private AuthenticationFacade authenticationFacade;
  @Autowired private YoutubeUploader youtubeUploader;
  @Value("${file.upload-dir}")
  private String uploadDir;

  @Override
  public Iterable<TeachingResourceResponse> getTeachingResourceByChapter(Long chapterId) {
    Iterable<TeachingResource> teachingResources =
        teachingResourceRepository.findByResourceIdAndType(
            chapterId, Chapter.class.getSimpleName());

    List<TeachingResourceResponse> teachingResourceResponses = new ArrayList<>();
    for (TeachingResource resource : teachingResources) {
      teachingResourceResponses.add(this.teachingResourceToTeachingResourceResponse(resource));
    }
    return teachingResourceResponses;
  }

  @Override
  @Transactional
  public TeachingResourceResponse uploadTeachingResource(
      TeachingResourceDTO teachingResourceDTO, MultipartFile file) {

    if (!isFileValid(file)) {
      throw new MediaTypeNotSupportedException(Translator.toLocale("MediaType.notsupported"));
    }

    TeachingResource teachingResource = saveTeachingResource(teachingResourceDTO, file);

    String fileUrl = uploadFile(teachingResource, file);

    teachingResource.setFileUrl(fileUrl);

    teachingResource = teachingResourceRepository.save(teachingResource);

    return this.teachingResourceToTeachingResourceResponse(teachingResource);
  }

  // private method

  private ResourceNotFoundException notFoundException(Long id) {
    return new ResourceNotFoundException(Translator.toLocale("TeachingResource.id.notfound"));
  }

  private TeachingResource saveTeachingResource(
      TeachingResourceDTO teachingResourceDTO, MultipartFile file) {

    TeachingResource teachingResource =
        teachingResourceDTOToTeachingResource(teachingResourceDTO, file);
    return teachingResourceRepository.save(teachingResource);
  }

  private String uploadFile(TeachingResource teachingResource, MultipartFile file) {

    String mediaType = file.getContentType();

    if (mediaType.startsWith("video/")) {
      Video returnVideo = uploadToYoutube(teachingResource, file);
      return returnVideo.getId();
    } else {
      return saveFileToLocalFileSystem(teachingResource, file);
    }
  }

  private Video uploadToYoutube(TeachingResource teachingResource, MultipartFile file) {
    return youtubeUploader.upload(file, teachingResource);
  }

  private String saveFileToLocalFileSystem(TeachingResource teachingResource, MultipartFile file) {

    String fileName = file.getOriginalFilename();

    // create a directory with resource type, eg: TeachingResource
    Path resourceTypePath =
        Paths.get(uploadDir, TeachingResource.class.getSimpleName()).normalize();
    try {
      Files.createDirectories(resourceTypePath);
    } catch (Exception exp) {
      throw new DirectoryCreationException(Translator.toLocale("Directory.notcreated"));
    }

    // create a directory with database id of teaching resource
    Path resourceIdPath = resourceTypePath.resolve(teachingResource.getId().toString());
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
    return targetLocation.toString();
  }

  private TeachingResource teachingResourceDTOToTeachingResource(
      TeachingResourceDTO teachingResourceDTO, MultipartFile file) {

    TeachingResource teachingResource = new TeachingResource();

    teachingResource.setTitle(teachingResourceDTO.getTitle());
    teachingResource.setDescription(teachingResourceDTO.getDescription());
    teachingResource.setResourceOwnerId(teachingResourceDTO.getResourceOwnerId());
    teachingResource.setResourceOwnerType(teachingResourceDTO.getResourceOwnerType());
    teachingResource.setPrivacyStatus(teachingResourceDTO.getPrivacyStatus());

    teachingResource.setFileName(file.getOriginalFilename());
    teachingResource.setContentType(file.getContentType());
    teachingResource.setFileSize(file.getSize());

    CustomUserPrincipal principal =
        (CustomUserPrincipal) authenticationFacade.getAuthentication().getPrincipal();
    teachingResource.setUser(principal.getUser());

    teachingResource.setTags(tagService.findOrCreateByNames(teachingResourceDTO.getTagNames()));

    return teachingResource;
  }

  private boolean isFileValid(MultipartFile file) {
    String mediaType = file.getContentType();

    for (String amt : ALLOWED_MEDIA_TYPES) {
      if (mediaType.startsWith(amt)) return true;
    }
    return false;
  }

  private TeachingResourceResponse teachingResourceToTeachingResourceResponse(
      TeachingResource teachingResource) {
    TeachingResourceResponse response = new TeachingResourceResponse();
    response.setId(teachingResource.getId());
    response.setTitle(teachingResource.getTitle());
    response.setDescription(teachingResource.getDescription());
    response.setFileName(teachingResource.getFileName());
    response.setContentType(teachingResource.getContentType());
    response.setHostingType(teachingResource.getHostingType());
    response.setPrivacyStatus(teachingResource.getPrivacyStatus());
    response.setResourceOwnerId(teachingResource.getResourceOwnerId());
    response.setResourceOwnerType(teachingResource.getResourceOwnerType());
    response.setFileSize(teachingResource.getFileSize());
    response.setUploaderId(teachingResource.getUser().getId());

    List<TagResponse> tagResponses = new ArrayList<>();
    for (Tag tag : teachingResource.getTags()) {
      tagResponses.add(tagService.tagToTagResponse(tag));
    }

    response.setTags(tagResponses);
    return response;
  }
}
