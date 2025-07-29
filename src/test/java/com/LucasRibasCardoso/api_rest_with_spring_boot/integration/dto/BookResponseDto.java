package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookResponseDto {

  private Long id;
  private String title;
  private String author;
  private LocalDate launchDate;
  private BigDecimal price;

  public BookResponseDto() {}

  public BookResponseDto(
      Long id, String title, String author, LocalDate launchDate, BigDecimal price) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.launchDate = launchDate;
    this.price = price;
  }
}
