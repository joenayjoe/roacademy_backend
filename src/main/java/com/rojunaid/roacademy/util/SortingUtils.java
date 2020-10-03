package com.rojunaid.roacademy.util;

import org.springframework.data.domain.Sort;

public class SortingUtils {

  public static Sort SortBy(String order) {
    int index = order.lastIndexOf("_");
    String field = order.substring(0, index);
    String dir = order.substring(index + 1);

    return dir.equalsIgnoreCase("desc") ? Sort.by(field).descending() : Sort.by(field).ascending();
  }
}
