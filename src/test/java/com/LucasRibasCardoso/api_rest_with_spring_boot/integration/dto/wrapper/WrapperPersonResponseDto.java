package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;

public class WrapperPersonResponseDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @JsonProperty("_embedded")
  private PersonEmbeddedResponseDto embedded;

  public WrapperPersonResponseDto() {}

  public PersonEmbeddedResponseDto getEmbedded() {
    return embedded;
  }

  public void setEmbedded(PersonEmbeddedResponseDto embedded) {
    this.embedded = embedded;
  }
}
