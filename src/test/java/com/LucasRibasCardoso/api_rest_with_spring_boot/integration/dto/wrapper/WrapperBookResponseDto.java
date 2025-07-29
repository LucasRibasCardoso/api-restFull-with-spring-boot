package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;

public class WrapperBookResponseDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @JsonProperty("_embedded")
  private BookEmbeddedResponseDto embedded;

  public WrapperBookResponseDto() {}

  public BookEmbeddedResponseDto getEmbedded() {
    return this.embedded;
  }

  public void setEmbedded(BookEmbeddedResponseDto embedded) {
    this.embedded = embedded;
  }
}
