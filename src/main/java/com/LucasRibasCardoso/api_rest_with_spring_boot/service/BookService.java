package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.controller.BookController;
import com.LucasRibasCardoso.api_rest_with_spring_boot.controller.PersonController;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.BookMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Book;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  public BookService(BookRepository bookRepository, BookMapper bookMapper) {
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
  }

  @Transactional(readOnly = true)
  public BookResponseDto findById(Long id) {
    logger.info("Get Book with id: {}", id);

    BookResponseDto bookResponseDto =
        bookRepository
            .findById(id)
            .map(bookMapper::toDto)
            .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

    addHateoasLinks(bookResponseDto);
    return bookResponseDto;
  }

  @Transactional(readOnly = true)
  public List<BookResponseDto> findAll() {
    logger.info("Get all Books");

    List<BookResponseDto> listOfResponseBookDto =
        bookRepository.findAll().stream().map(bookMapper::toDto).toList();

    listOfResponseBookDto.forEach(this::addHateoasLinks);
    return listOfResponseBookDto;
  }

  @Transactional
  public BookResponseDto save(BookCreateDto createDto) {
    logger.info("Saving Book");
    if (createDto == null) {
      throw new RequiredObjectIsNullException();
    }

    String author = createDto.author();
    String title = createDto.title();
    if (bookRepository.existsByAuthorIgnoreCase(author)
        && bookRepository.existsByTitleIgnoreCase(title)) {
      throw new BookAlreadyExistsException(
          "This book already exists! Title: " + title + ", Author: " + author);
    }

    Book savedBook = bookRepository.save(bookMapper.toEntity(createDto));
    BookResponseDto responseDto = bookMapper.toDto(savedBook);

    addHateoasLinks(responseDto);
    return responseDto;
  }

  @Transactional
  public BookResponseDto update(Long id, BookUpdateDto updateDto) {
    logger.info("Updating Book");

    if (updateDto == null) {
      throw new RequiredObjectIsNullException();
    }

    Book bookEntity =
        bookRepository
            .findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

    bookMapper.updateEntityFromDto(updateDto, bookEntity);
    BookResponseDto responseDto = bookMapper.toDto(bookRepository.save(bookEntity));

    addHateoasLinks(responseDto);
    return responseDto;
  }

  @Transactional
  public void delete(Long id) {
    logger.info("Deleting Book with id: {}", id);

    Book book =
        bookRepository
            .findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

    bookRepository.delete(book);
  }

  private void addHateoasLinks(BookResponseDto responseDto) {
    responseDto.add(
        linkTo(methodOn(BookController.class).findById(responseDto.getId()))
            .withSelfRel()
            .withType("GET"));
    responseDto.add(
        linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
    responseDto.add(
        linkTo(methodOn(BookController.class).save(null)).withRel("create").withType("POST"));
    responseDto.add(
        linkTo(methodOn(BookController.class).update(responseDto.getId(), null))
            .withRel("update")
            .withType("PATCH"));
    responseDto.add(
        linkTo(methodOn(BookController.class).delete(responseDto.getId()))
            .withRel("delete")
            .withType("DELETE"));
  }
}
