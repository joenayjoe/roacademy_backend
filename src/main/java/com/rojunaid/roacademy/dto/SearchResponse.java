package com.rojunaid.roacademy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class SearchResponse<T> {
  private Long totalPage;
  private Long starPage;
  private Long endPage;
  private Iterable<T> result = new ArrayList<>();
}
