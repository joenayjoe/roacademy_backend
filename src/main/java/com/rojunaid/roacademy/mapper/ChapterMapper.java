package com.rojunaid.roacademy.mapper;

import com.rojunaid.roacademy.dto.ChapterDTO;
import com.rojunaid.roacademy.models.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChapterMapper {

  ChapterMapper INSTANCE = Mappers.getMapper(ChapterMapper.class);

  Chapter chapterDTOToChapter(ChapterDTO chapterDTO);

  ChapterDTO chapterToChapterDTO(Chapter chapter);
}
