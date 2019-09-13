package com.rojunaid.roacademy.controllers;

import com.rojunaid.roacademy.dto.TeachingResourceDTO;
import com.rojunaid.roacademy.dto.TeachingResourceResponse;
import com.rojunaid.roacademy.models.TeachingResource;
import com.rojunaid.roacademy.services.TeachingResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping(
      value = "/uploadFile",
      consumes = {"multipart/form-data"})
  public ResponseEntity<TeachingResourceResponse> uploadFile(
      @Valid @RequestPart(value = "fileInfo") TeachingResourceDTO teachingResourceDTO,
      @NotNull @NotBlank @RequestPart MultipartFile file) {
    TeachingResourceResponse teachingResourceResponse = teachingResourceService.uploadTeachingResource(teachingResourceDTO, file);
    return new ResponseEntity<>(teachingResourceResponse, HttpStatus.CREATED);
  }
}
