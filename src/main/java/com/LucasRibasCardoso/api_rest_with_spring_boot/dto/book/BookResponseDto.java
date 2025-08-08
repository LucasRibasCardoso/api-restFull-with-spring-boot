package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "books")
public class BookResponseDto extends RepresentationModel<BookResponseDto> {

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDate getLaunchDate() {
    return launchDate;
  }

  public void setLaunchDate(LocalDate launchDate) {
    this.launchDate = launchDate;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
