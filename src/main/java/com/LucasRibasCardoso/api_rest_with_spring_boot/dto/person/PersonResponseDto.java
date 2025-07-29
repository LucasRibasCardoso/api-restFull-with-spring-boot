package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "people")
public class PersonResponseDto extends RepresentationModel<PersonResponseDto> {
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
