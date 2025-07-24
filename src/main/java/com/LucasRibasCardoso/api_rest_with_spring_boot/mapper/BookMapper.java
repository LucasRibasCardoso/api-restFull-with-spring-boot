package com.LucasRibasCardoso.api_rest_with_spring_boot.mapper;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

  BookResponseDto toDto(Book entity);

  @Mapping(target = "id", ignore = true)
  Book toEntity(BookCreateDto dto);

  void updateEntityFromDto(BookUpdateDto dto, @MappingTarget Book entity);
}
