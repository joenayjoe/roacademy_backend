package com.rojunaid.roacademy.dto.mapper;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.models.Chapter;

public class ChapterMapper {

  public static Chapter chapterDTOToChapter(ChapterDTO chapterDTO) {
    Chapter chapter = new Chapter();
    chapter.setName(chapterDTO.getName());
    return chapter;
  }
}
