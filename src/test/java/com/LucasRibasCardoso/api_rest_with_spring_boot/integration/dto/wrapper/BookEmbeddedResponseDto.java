package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper;

import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.BookResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class BookEmbeddedResponseDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @JsonProperty("books")
  private List<BookResponseDto> books;

  public BookEmbeddedResponseDto() {}

  public List<BookResponseDto> getBooks() {
    return books;
  }

  public void setBooks(List<BookResponseDto> books) {
    this.books = books;
  }
}
