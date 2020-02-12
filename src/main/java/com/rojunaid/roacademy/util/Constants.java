package com.rojunaid.roacademy.util;

import org.springframework.stereotype.Component;

@Component
public final class Constants {
  private Constants() {
    // restrict instantiation
  }

  public static final String DEFAULT_SORTING = "id_desc";
  public static final String DEFAULT_PAGE_SIZE = "5";
  public static final String DEFAULT_PAGE = "0";
  public static final String DEFAULT_COURSE_STATUS = "PUBLISHED";
}
