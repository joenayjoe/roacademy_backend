package com.rojunaid.roacademy.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Helper {

  private static final String[] VIDEO_FORMAT = {"mp4", "avi", "mpeg4", "mov", "avi", "flv"};
  private static final String[] IMAGE_FORMAT = {"jpeg", "png"};
  private static final String[] DOCUMENT_FORMAT = {"pdf", "doc", "docx"};

  public static Optional<String> getFileExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }

  public static boolean isFileSupported(String filename) {
    List<String> allFormat = new ArrayList<>(Arrays.asList(VIDEO_FORMAT));
    allFormat.addAll(Arrays.asList(IMAGE_FORMAT));
    allFormat.addAll(Arrays.asList(DOCUMENT_FORMAT));

    return allFormat.stream().anyMatch(getFileExtension(filename)::equals);
  }

  public static boolean isVideoFile(String extension) {
    return Arrays.stream(VIDEO_FORMAT).anyMatch(extension::equals);
  }

  public static HttpServletRequest getCurrentRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    Assert.state(
        requestAttributes != null, "Could not find current request via RequestContextHolder");
    Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
    HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
    Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
    return servletRequest;
  }

  public static String getBaseUrl() {
    HttpServletRequest currentRequest = getCurrentRequest();
    String reqURL = currentRequest.getRequestURL().toString();
    String reqURI = currentRequest.getRequestURI();
    return reqURL.replace(reqURI, "");
  }

  public static String buildURL(Class klass, String methodName, Long methodParam) {

    UriComponents uriComponents =
        MvcUriComponentsBuilder.fromMethodName(klass, methodName, methodParam).build();
    return uriComponents.encode().toUriString();
  }

  public static String buildURL(
      Class klass, String methodName, Long methodParams, Long pathVariable) {
    UriComponents uriComponents =
        MvcUriComponentsBuilder.fromMethodName(klass, methodName, methodParams)
            .buildAndExpand(pathVariable);
    return uriComponents.encode().toUriString();
  }

  public static String buildURL(
      Class klass, String methodName, Long methodParam1, Long methodParam2, Long pathVariable) {
    UriComponents uriComponents =
        MvcUriComponentsBuilder.fromMethodName(klass, methodName, methodParam1, methodParam2)
            .buildAndExpand(pathVariable);
    return uriComponents.encode().toUriString();
  }
}
