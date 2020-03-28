package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class SearchResponse {
  private Long id;
  private String name;
  private String type;
  private String url;
}
