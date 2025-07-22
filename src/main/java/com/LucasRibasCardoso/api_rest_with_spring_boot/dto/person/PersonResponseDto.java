package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import org.springframework.hateoas.RepresentationModel;

public class PersonResponseDto extends RepresentationModel<PersonResponseDto> {
  private Long id;
  private String firstName;
  private String lastName;
  private String cpf;
  private Gender gender;

  public PersonResponseDto() {}

  public PersonResponseDto(Long id, String firstName, String lastName, String cpf, Gender gender) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cpf = cpf;
    this.gender = gender;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getCpf() {
    return cpf;
  }

  public Gender getGender() {
    return gender;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }
}
