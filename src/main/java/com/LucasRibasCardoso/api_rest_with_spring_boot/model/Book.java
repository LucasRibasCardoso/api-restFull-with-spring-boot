package com.LucasRibasCardoso.api_rest_with_spring_boot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "tb_books")
public class Book implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String author;

  @Column(name = "launch_date", nullable = false)
  private LocalDate launchDate;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false, length = 100)
  private String title;

  public Book() {}

  public Book(Long id, String author, LocalDate launchDate, BigDecimal price, String title) {
    this.id = id;
    this.author = author;
    this.launchDate = launchDate;
    this.price = price;
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Book book)) return false;
    return Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(title, book.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, author, title);
  }
}
