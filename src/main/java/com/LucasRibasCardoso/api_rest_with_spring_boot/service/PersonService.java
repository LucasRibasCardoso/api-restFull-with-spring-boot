package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.LucasRibasCardoso.api_rest_with_spring_boot.controller.PersonController;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.FieldExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.ValidationExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.CustomValidationException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.ExportFileException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.FileStorageException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.InvalidFileException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.contract.FileExporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.factory.FileExporterFactory;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.contract.FileImporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.factory.FileImporterFactory;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.PersonMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.PersonRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PersonService {

  private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

  private final PersonRepository repository;
  private final PersonMapper personMapper;
  private final FileImporterFactory fileImporterFactory;
  private final FileExporterFactory fileExporterFactory;
  private final Validator validator;

  public PersonService(
      PersonRepository repository,
      PersonMapper personMapper,
      FileImporterFactory fileImporterFactory,
      Validator validator,
      FileExporterFactory fileExporterFactory) {
    this.repository = repository;
    this.personMapper = personMapper;
    this.fileImporterFactory = fileImporterFactory;
    this.validator = validator;
    this.fileExporterFactory = fileExporterFactory;
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
  public Resource exportPage(Pageable pageable, String acceptHeader) {
    logger.info("Exporting all persons");

    Page<PersonResponseDto> pageOfPersonResponseDto =
        repository
            .findAll(pageable)
            .map(personMapper::toDto)
            .map(
                dto -> {
                  addHateoasLinks(dto);
                  return dto;
                });

    try {
      FileExporter exporter = this.fileExporterFactory.getExporter(acceptHeader);
      return exporter.exportFile(pageOfPersonResponseDto.stream().toList());
    } catch (Exception e) {
      throw new ExportFileException("Failed to export persons");
    }
  }

  @Transactional
  public Page<PersonResponseDto> findByName(String firstName, Pageable pageable) {
    logger.info("Get Persons with firstName: {}", firstName);

    return repository
        .findByFirstNameContainingIgnoreCase(firstName, pageable)
        .map(personMapper::toDto)
        .map(
            dto -> {
              addHateoasLinks(dto);
              return dto;
            });
  }

  @Transactional(readOnly = true)
  public Page<PersonResponseDto> findAll(Pageable pageable) {
    logger.info("Get all person");

    return repository
        .findAll(pageable)
        .map(personMapper::toDto)
        .map(
            dto -> {
              addHateoasLinks(dto);
              return dto;
            });
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

  @Transactional
  public PersonResponseDto disablePerson(Long id) {
    logger.info("Disabling Person with id: {}", id);

    repository
        .findById(id)
        .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    repository.disablePerson(id);

    Person personEntity = repository.findById(id).get();

    PersonResponseDto personResponse = personMapper.toDto(personEntity);
    addHateoasLinks(personResponse);
    return personResponse;
  }

  @Transactional
  public PersonResponseDto enablePerson(Long id) {
    logger.info("Enabling Person with id: {}", id);

    repository
        .findById(id)
        .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

    repository.enablePerson(id);
    Person personEntity = repository.findById(id).get();

    PersonResponseDto personResponse = personMapper.toDto(personEntity);
    addHateoasLinks(personResponse);
    return personResponse;
  }

  @Transactional
  public List<PersonResponseDto> massCreation(MultipartFile file) {
    validateFile(file);
    List<PersonCreateDto> listOfDtos = importPersonCreateDtos(file);
    validatePersonCreateDtos(listOfDtos);

    Set<String> listOfCpf =
        listOfDtos.stream().map(PersonCreateDto::cpf).collect(Collectors.toSet());
    checkExistingCpfs(listOfCpf);

    List<Person> savedEntities =
        repository.saveAll(listOfDtos.stream().map(personMapper::toEntity).toList());

    return savedEntities.stream().map(personMapper::toDto).toList();
  }

  private void addHateoasLinks(PersonResponseDto responseDto) {
    responseDto.add(
        linkTo(methodOn(PersonController.class).findById(responseDto.getId()))
            .withSelfRel()
            .withType("GET"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).findAll(0, 10, "desc", "id", null))
            .withRel("all")
            .withType("GET"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).save(null)).withRel("create").withType("POST"));
    responseDto.add(
        linkTo(methodOn(PersonController.class))
            .slash("massCreation")
            .withRel("massCreation")
            .withType("POST"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).update(responseDto.getId(), null))
            .withRel("update")
            .withType("PATCH"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).disablePerson(responseDto.getId()))
            .withRel("disable")
            .withType("PATCH"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).enablePerson(responseDto.getId()))
            .withRel("enable")
            .withType("PATCH"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).delete(responseDto.getId()))
            .withRel("delete")
            .withType("DELETE"));
    responseDto.add(
        linkTo(methodOn(PersonController.class).exportPage(0, 10, "desc", "id", null))
            .withRel("exportPage")
            .withType("DELETE"));
  }

  private void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new InvalidFileException("File is empty");
    }
    Optional.ofNullable(file.getOriginalFilename())
        .filter(name -> !name.isBlank())
        .orElseThrow(() -> new InvalidFileException("Filename is missing"));
  }

  private List<PersonCreateDto> importPersonCreateDtos(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      FileImporter importer = fileImporterFactory.getImporter(file.getOriginalFilename());
      return importer.importFile(inputStream);
    } catch (IOException ex) {
      throw new FileStorageException("Failed to read file", ex);
    } catch (Exception ex) {
      throw new FileStorageException("Failed to process file", ex);
    }
  }

  private void validatePersonCreateDtos(List<PersonCreateDto> dtos) {
    Set<ConstraintViolation<PersonCreateDto>> violations = new HashSet<>();
    for (PersonCreateDto dto : dtos) {
      violations.addAll(validator.validate(dto));
    }
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void checkExistingCpfs(Set<String> cpfs) {
    Set<String> existing =
        repository.findAllByCpfIn(cpfs).stream().map(Person::getCpf).collect(Collectors.toSet());

    if (!existing.isEmpty()) {
      List<FieldExceptionResponse> errors =
          existing.stream()
              .map(cpf -> new FieldExceptionResponse("cpf", "CPF already exists: " + cpf))
              .toList();
      ValidationExceptionResponse response =
          new ValidationExceptionResponse(
              Instant.now(), HttpStatus.BAD_REQUEST.value(), "Duplicate CPFs found", null, errors);
      throw new CustomValidationException(response);
    }
  }
}
