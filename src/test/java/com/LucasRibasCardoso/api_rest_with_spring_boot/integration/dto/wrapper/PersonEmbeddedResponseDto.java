package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper;

import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.PersonResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PersonEmbeddedResponseDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @JsonProperty("people")
  private List<PersonResponseDto> people;

  public PersonEmbeddedResponseDto() {}

  public List<PersonResponseDto> getPeople() {
    return people;
  }

  public void setPeople(List<PersonResponseDto> people) {
    this.people = people;
  }
}
