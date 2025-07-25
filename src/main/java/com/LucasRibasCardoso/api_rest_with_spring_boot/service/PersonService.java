package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.LucasRibasCardoso.api_rest_with_spring_boot.controller.PersonController;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.PersonMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.PersonRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

  private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

  private final PersonRepository repository;
  private final PersonMapper personMapper;

  public PersonService(PersonRepository repository, PersonMapper personMapper) {
    this.repository = repository;
    this.personMapper = personMapper;
  }

  @Transactional(readOnly = true)
  public PersonResponseDto findById(Long id) {
    logger.info("Get Person with id: {}", id);

    PersonResponseDto personResponseDto =
        repository
            .findById(id)
            .map(personMapper::toDto)
            .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    addHateoasLinks(personResponseDto);
    return personResponseDto;
  }

  @Transactional(readOnly = true)
  public List<PersonResponseDto> findAll() {
    logger.info("Get all Persons");

    List<PersonResponseDto> persons =
        repository.findAll().stream().map(personMapper::toDto).toList();

    persons.forEach((this::addHateoasLinks));
    return persons;
  }

  @Transactional
  public PersonResponseDto save(PersonCreateDto createDto) {
    if (createDto == null) {
      throw new RequiredObjectIsNullException();
    }

    logger.info("Saving Person");
    if (repository.existsByCpf(createDto.cpf())) {
      throw new PersonAlreadyExistsException("This CPF is already in use");
    }

    Person savedPerson = repository.save(personMapper.toEntity(createDto));
    PersonResponseDto personResponse = personMapper.toDto(savedPerson);

    addHateoasLinks(personResponse);
    return personResponse;
  }

  @Transactional
  public PersonResponseDto update(Long id, PersonUpdateDto updateDto) {
    if (updateDto == null) {
      throw new RequiredObjectIsNullException();
    }

    logger.info("Updating Person");
    Person personEntity =
        repository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    personMapper.updateEntityFromDto(updateDto, personEntity);
    PersonResponseDto personResponse = personMapper.toDto(repository.save(personEntity));

    addHateoasLinks(personResponse);
    return personResponse;
  }

  @Transactional
  public void delete(Long id) {
    logger.info("Deleting Person with id: {}", id);

    Person entity =
        repository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    repository.delete(entity);
  }

  private void addHateoasLinks(PersonResponseDto responseDto) {
    responseDto.add(
        linkTo(methodOn(PersonController.class).findById(responseDto.getId()))
            .withSelfRel()
            .withType("GET"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).save(null)).withRel("create").withType("POST"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).update(responseDto.getId(), null))
            .withRel("update")
            .withType("PATCH"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).delete(responseDto.getId()))
            .withRel("delete")
            .withType("DELETE"));
  }
}
