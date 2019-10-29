package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.UserRepository;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.TeachingResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
public class FileUploadController {

  @Autowired private TeachingResourceService teachingResourceService;
  @Autowired private FileUploadService fileUploadService;
  @Autowired private UserRepository userRepository;

  @PostMapping(
      value = "/uploadFile",
      consumes = {"multipart/form-data"})
  public ResponseEntity<TeachingResourceResponse> uploadFile(
      @Valid @RequestPart(value = "fileInfo") TeachingResourceDTO teachingResourceDTO,
      @NotNull @NotBlank @RequestPart MultipartFile file) {
    TeachingResourceResponse teachingResourceResponse =
        teachingResourceService.uploadTeachingResource(teachingResourceDTO, file);
    return new ResponseEntity<>(teachingResourceResponse, HttpStatus.CREATED);
  }

  @GetMapping("/User/{userId}/{fileName:.+}")
  public ResponseEntity<Resource> getProfilePhoto(
      @PathVariable Long userId, @PathVariable String fileName, HttpServletRequest request) {

    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    String imageUrl = user.getImageUrl();

    if (!request.getRequestURI().equals(imageUrl)) {
      throw new ResourceNotFoundException("Resource not found");
    }

    Resource resource = fileUploadService.getFileAsResource(imageUrl);
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      System.out.println("Count not determine mimeType");
    }

    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
