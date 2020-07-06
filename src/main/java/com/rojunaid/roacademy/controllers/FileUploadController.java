package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.TeachingResourceRequest;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.repositories.LectureResourceRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.TeachingResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
public class FileUploadController {

  @Autowired private TeachingResourceService teachingResourceService;
  @Autowired private FileUploadService fileUploadService;
  @Autowired private UserRepository userRepository;
  @Autowired private LectureResourceRepository lectureResourceRepository;

  @PostMapping(
      value = "/uploadFile",
      consumes = {"multipart/form-data"})
  public ResponseEntity<TeachingResourceResponse> uploadFile(
      @Valid @RequestPart(value = "fileInfo") TeachingResourceRequest teachingResourceRequest,
      @NotNull @NotBlank @RequestPart MultipartFile file) {
    TeachingResourceResponse teachingResourceResponse =
        teachingResourceService.uploadTeachingResource(teachingResourceRequest, file);
    return new ResponseEntity<>(teachingResourceResponse, HttpStatus.CREATED);
  }

  //  @GetMapping("/User/{userId}/{fileName:.+}")
  //  public ResponseEntity<Resource> getProfilePhoto(
  //      @PathVariable Long userId, @PathVariable String fileName, HttpServletRequest request) {
  //
  //    User user = userRepository.findById(userId).orElse(null);
  //    if (user == null) {
  //      throw new ResourceNotFoundException("Resource not found");
  //    }
  //    String imageUrl = user.getImageUrl();
  //
  //    if (!request.getRequestURI().equals(imageUrl)) {
  //      throw new ResourceNotFoundException("Resource not found");
  //    }
  //
  //    Resource resource = fileUploadService.getFileAsResource(imageUrl);
  //    String contentType = null;
  //    try {
  //      contentType =
  // request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
  //    } catch (IOException ex) {
  //      System.out.println("Count not determine mimeType");
  //    }
  //
  //    if (contentType == null) {
  //      contentType = "application/octet-stream";
  //    }
  //
  //    return ResponseEntity.ok()
  //        .contentType(MediaType.parseMediaType(contentType))
  //        .header(
  //            HttpHeaders.CONTENT_DISPOSITION,
  //            "attachment; filename=\"" + resource.getFilename() + "\"")
  //        .body(resource);
  //  }

  //  @GetMapping("/resources/{resourceClass}/{resourceId}/{resourceName:.+}")
  //  public ResponseEntity<Resource> getFileAsResource(
  //      @PathVariable String resourceClass,
  //      @PathVariable Long resourceId,
  //      @PathVariable String resourceName,
  //      HttpServletRequest request) {
  //
  //    String receivedResourceUrl =
  //        "/resources/" + resourceClass + "/" + resourceId + "/" + resourceName;
  //    String resourceUrl = "";
  //    if (resourceClass.equals(User.class.getSimpleName().toLowerCase())) {
  //      User user = userRepository.findById(resourceId).orElse(null);
  //      if (user == null) {
  //        throw new ResourceNotFoundException(
  //            Translator.toLocale("File.notfound", new Object[] {receivedResourceUrl}));
  //      }
  //      resourceUrl = user.getImageUrl();
  //    } else if (resourceClass.equals(VideoResource.class.getSimpleName().toLowerCase())) {
  //      VideoResource videoResource = lectureResourceRepository.findById(resourceId).orElse(null);
  //      if (videoResource == null) {
  //        throw new ResourceNotFoundException(
  //            Translator.toLocale("File.notfound", new Object[] {receivedResourceUrl}));
  //      }
  //      resourceUrl = videoResource.getFileUrl();
  //    }
  //
  //    if (!receivedResourceUrl.equals(resourceUrl)) {
  //      throw new ResourceNotFoundException(
  //          Translator.toLocale("File.notfound", new Object[] {receivedResourceUrl}));
  //    }
  //
  //    Resource resource = fileUploadService.getFileAsResource(resourceUrl);
  //    String contentType = null;
  //    try {
  //      contentType =
  // request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
  //    } catch (IOException ex) {
  //      System.out.println("Count not determine mimeType");
  //    }
  //
  //    if (contentType == null) {
  //      contentType = "application/octet-stream";
  //    }
  //
  //    return ResponseEntity.ok()
  //        .contentType(MediaType.parseMediaType(contentType))
  //        .header(
  //            HttpHeaders.CONTENT_DISPOSITION,
  //            "attachment; filename=\"" + resource.getFilename() + "\"")
  //        .body(resource);
  //  }
}
