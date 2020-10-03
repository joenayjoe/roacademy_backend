package com.rojunaid.roacademy.auth.oauth2.imgur;

import com.google.api.client.http.*;
import com.google.common.reflect.TypeToken;
import com.rojunaid.roacademy.auth.oauth2.Auth;
import com.rojunaid.roacademy.auth.oauth2.UploadedResourceInfo;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.MediaUploadException;
import com.rojunaid.roacademy.models.AuthProvider;
import com.rojunaid.roacademy.models.OAuth2Credential;
import com.rojunaid.roacademy.util.HttpClientUtils;
import com.rojunaid.roacademy.util.OAuth2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImgurApiManager {

  public static final String UPLOAD_URL = "https://api.imgur.com/3/upload";
  public static final String DELETE_URL = "https://api.imgur.com/3/image";
  @Autowired private Auth authProvider;

  public UploadedResourceInfo uploadImage(MultipartFile file) {
    try {

      OAuth2Credential credential = this.getCredential();

      HttpRequestFactory requestFactory = HttpClientUtils.getRequestFactory();

      GenericUrl url = new GenericUrl(UPLOAD_URL);

      Map<String, String> parameters = new HashMap<>();
      parameters.put("name", file.getOriginalFilename());
      parameters.put("title", file.getOriginalFilename());

      MultipartContent content =
          new MultipartContent()
              .setMediaType(
                  new HttpMediaType("multipart/form-data")
                      .setParameter("boundary", "__END_OF_PART__"));

      // parameters
      for (String name : parameters.keySet()) {
        MultipartContent.Part part =
            new MultipartContent.Part(new ByteArrayContent(null, parameters.get(name).getBytes()));
        part.setHeaders(
            new HttpHeaders()
                .set("Content-Disposition", String.format("form-data; name=\"%s\"", name)));
        content.addPart(part);
      }

      // file
      MultipartContent.Part part =
          new MultipartContent.Part()
              .setContent(new InputStreamContent(file.getContentType(), file.getInputStream()));
      part.setHeaders(
          new HttpHeaders()
              .set(
                  "Content-Disposition",
                  String.format(
                      "form-data; name=\"image\"; filename=\"%s\"", file.getOriginalFilename())));
      content.addPart(part);

      HttpRequest request = requestFactory.buildPostRequest(url, content);
      HttpHeaders headers = request.getHeaders();
      headers.setAuthorization("Bearer " + credential.getAccessToken());
      request.setHeaders(headers);

      Type type = new TypeToken<ImgurBasicResponse>() {}.getType();
      HttpResponse response = request.execute();
      ImgurBasicResponse imgurBasicResponse = (ImgurBasicResponse) response.parseAs(type);
      UploadedResourceInfo resourceInfo = new UploadedResourceInfo();
      resourceInfo.setResourceId(imgurBasicResponse.getData().getId());
      resourceInfo.setResourceDeleteHash(imgurBasicResponse.getData().getImageDeleteHash());
      resourceInfo.setResourceUrl(imgurBasicResponse.getData().getImageUrl());
      return resourceInfo;
    } catch (IOException e) {
      throw new MediaUploadException(e.getLocalizedMessage());
    }
  }

  public void deleteImage(String imageId) {
    try {
      OAuth2Credential credential = this.getCredential();
      HttpRequestFactory requestFactory = HttpClientUtils.getRequestFactory();
      GenericUrl requestUrl = new GenericUrl(String.format("%s/%s", DELETE_URL, imageId));
      HttpRequest request = requestFactory.buildDeleteRequest(requestUrl);
      HttpHeaders headers = request.getHeaders();
      headers.setAuthorization("Bearer " + credential.getAccessToken());
      request.setHeaders(headers);
      request.execute();

    } catch (IOException e) {
      throw new MediaUploadException(e.getLocalizedMessage());
    }
  }

  private OAuth2Credential getCredential() throws IOException {

    OAuth2Credential credential =
        authProvider
            .getOAuth2Credential(AuthProvider.imgur)
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Imgur API is not configured. Please contact the admin."));

    String clientId = authProvider.getClientId(AuthProvider.imgur);
    String clientSecret = authProvider.getClientSecret(AuthProvider.imgur);
    String tokenUrl = authProvider.getTokenUri(AuthProvider.imgur);

    credential =
        OAuth2Utils.updateTokenIfNotValid(
            clientId, clientSecret, credential, tokenUrl, authProvider);
    return credential;
  }
}
