package com.LucasRibasCardoso.api_rest_with_spring_boot.mapper;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

  PersonResponseDto toDto(Person entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enabled", constant = "true")
  Person toEntity(PersonCreateDto dto);

  void updateEntityFromDto(PersonUpdateDto dto, @MappingTarget Person entity);
}
