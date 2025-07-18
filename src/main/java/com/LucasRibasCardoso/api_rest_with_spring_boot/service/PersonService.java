package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
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
  public Person getById(Long id) {
    logger.info("Get Person with id: {}", id);
    return repository
        .findById(id)
        .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));
  }

  @Transactional(readOnly = true)
  public List<Person> getAll() {
    logger.info("Get all Persons");
    return repository.findAll();
  }

  @Transactional
  public Person save(PersonCreateDto createDto) {
    logger.info("Saving Person: {}", createDto);

    if (repository.existsByCpf(createDto.cpf())) {
      throw new PersonAlreadyExistsException("Person cpf already exists");
    }
    Person personEntity = personMapper.toEntity(createDto);

    return repository.save(personEntity);
  }

  @Transactional
  public Person update(Long id, PersonUpdateDto updateDto) {
    logger.info("Updating Person: {}", updateDto);

    Person entity =
        repository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    personMapper.updateEntityFromDto(updateDto, entity);

    return repository.save(entity);
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
}
