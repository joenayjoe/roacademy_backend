package com.rojunaid.roacademy.util;

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
}
