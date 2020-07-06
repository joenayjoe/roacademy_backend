package com.rojunaid.roacademy.auth.oauth2;

import lombok.Data;

@Data
public class UploadedResourceInfo {

  private String resourceId;
  private String resourceDeleteHash;
  private String resourceUrl;
}
