package com.rojunaid.roacademy.services.impl;

import com.google.api.services.youtube.model.Video;
import com.rojunaid.roacademy.dto.TagResponse;
import com.rojunaid.roacademy.dto.TeachingResourceRequest;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.exception.MediaTypeNotSupportedException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Tag;
import com.rojunaid.roacademy.models.TeachingResource;
import com.rojunaid.roacademy.repositories.TeachingResourceRepository;
import com.rojunaid.roacademy.security.AuthenticationFacade;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.services.TeachingResourceService;
import com.rojunaid.roacademy.util.Translator;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeMetaData;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingResourceServiceImpl implements TeachingResourceService {

  private static final List<String> ALLOWED_MEDIA_TYPES =
      Arrays.asList("image/png", "image/jpeg", "video/", "application/pdf");
  @Autowired private TeachingResourceRepository teachingResourceRepository;
  @Autowired private TagService tagService;
  @Autowired private AuthenticationFacade authenticationFacade;
  @Autowired private YoutubeApiManager youtubeApiManager;
  @Autowired private FileUploadService fileUploadService;

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
      TeachingResourceRequest teachingResourceRequest, MultipartFile file) {

    if (!isFileValid(file)) {
      throw new MediaTypeNotSupportedException(Translator.toLocale("MediaType.notsupported"));
    }

    TeachingResource teachingResource = saveTeachingResource(teachingResourceRequest, file);

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
      TeachingResourceRequest teachingResourceRequest, MultipartFile file) {

    TeachingResource teachingResource =
        teachingResourceDTOToTeachingResource(teachingResourceRequest, file);
    return teachingResourceRepository.save(teachingResource);
  }

  private String uploadFile(TeachingResource teachingResource, MultipartFile file) {

    String mediaType = file.getContentType();

    if (mediaType.startsWith("video/")) {
      Video returnVideo = uploadToYoutube(teachingResource, file);
      return returnVideo.getId();
    } else {
      String trn = TeachingResource.class.getSimpleName();
      return fileUploadService.uploadToLocal(trn, teachingResource.getId(), file);
    }
  }

  private Video uploadToYoutube(TeachingResource teachingResource, MultipartFile file) {
    YoutubeMetaData metaData = new YoutubeMetaData();
    metaData.setTitle(teachingResource.getTitle());
    metaData.setDescription(teachingResource.getDescription());
    metaData.setStatus(teachingResource.getPrivacyStatus());
    metaData.setTags(
        teachingResource.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()));
    return youtubeApiManager.upload(metaData, file);
  }

  private TeachingResource teachingResourceDTOToTeachingResource(
      TeachingResourceRequest teachingResourceRequest, MultipartFile file) {

    TeachingResource teachingResource = new TeachingResource();

    teachingResource.setTitle(teachingResourceRequest.getTitle());
    teachingResource.setDescription(teachingResourceRequest.getDescription());
    teachingResource.setResourceOwnerId(teachingResourceRequest.getResourceOwnerId());
    teachingResource.setResourceOwnerType(teachingResourceRequest.getResourceOwnerType());
    teachingResource.setPrivacyStatus(teachingResourceRequest.getPrivacyStatus());

    teachingResource.setFileName(file.getOriginalFilename());
    teachingResource.setContentType(file.getContentType());
    teachingResource.setFileSize(file.getSize());

    CustomUserPrincipal principal =
        (CustomUserPrincipal) authenticationFacade.getAuthentication().getPrincipal();
    teachingResource.setUser(principal.getUser());

    teachingResource.setTags(tagService.findOrCreateByNames(teachingResourceRequest.getTagNames()));

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
