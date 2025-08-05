package com.LucasRibasCardoso.api_rest_with_spring_boot.mapper;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  SignUpResponseDto toDto(User entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "permissions", ignore = true)
  @Mapping(target = "enabled", constant = "true")
  @Mapping(target = "accountNonExpired", constant = "true")
  @Mapping(target = "accountNonLocked", constant = "true")
  @Mapping(target = "credentialsNonExpired", constant = "true")
  User toEntity(SignUpRequestDto dto);
}
