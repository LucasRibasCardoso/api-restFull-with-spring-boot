package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String cpf;
  private Gender gender;
  private Boolean enabled;

  public PersonResponseDto() {}

  public PersonResponseDto(
      Long id, String firstName, String lastName, String cpf, Gender gender, Boolean enabled) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cpf = cpf;
    this.gender = gender;
    this.enabled = enabled;
  }
}
