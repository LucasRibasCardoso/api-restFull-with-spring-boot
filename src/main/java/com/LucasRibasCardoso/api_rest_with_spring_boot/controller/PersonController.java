package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.PersonControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.PersonService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController implements PersonControllerDocs {

  private final PersonService service;

  public PersonController(PersonService service) {
    this.service = service;
  }

  @GetMapping(
      value = "/{id}",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<PersonResponseDto> findById(@PathVariable Long id) {
    PersonResponseDto personResponseDto = service.findById(id);
    return ResponseEntity.ok(personResponseDto);
  }

  @GetMapping(
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<PagedModel<EntityModel<PersonResponseDto>>> findAll(
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "direction", defaultValue = "desc") String direction,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      PagedResourcesAssembler<PersonResponseDto> assembler) {

    Sort.Direction sortDirection =
        "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    Page<PersonResponseDto> personPage = service.findAll(pageable);

    return ResponseEntity.ok(assembler.toModel(personPage));
  }

  @GetMapping(
      value = "/findByName/{firstName}",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<PagedModel<EntityModel<PersonResponseDto>>> findByName(
      @PathVariable("firstName") String firstName,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "direction", defaultValue = "desc") String direction,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      PagedResourcesAssembler<PersonResponseDto> assembler) {

    Sort.Direction sortDirection =
        "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    Page<PersonResponseDto> personPage = service.findByName(firstName, pageable);

    return ResponseEntity.ok(assembler.toModel(personPage));
  }

  @PostMapping(
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<PersonResponseDto> save(
      @RequestBody @Valid PersonCreateDto personCreateDto) {
    PersonResponseDto savedPersonResponseDto = service.save(personCreateDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedPersonResponseDto.getId())
            .toUri();
    return ResponseEntity.created(location).body(savedPersonResponseDto);
  }

  @PatchMapping(
      value = "/{id}",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<PersonResponseDto> update(
      @PathVariable Long id, @RequestBody @Valid PersonUpdateDto personUpdateDto) {
    PersonResponseDto updatedPersonResponseDto = service.update(id, personUpdateDto);
    return ResponseEntity.ok(updatedPersonResponseDto);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/disable/{id}")
  @Override
  public ResponseEntity<PersonResponseDto> disablePerson(@PathVariable Long id) {
    PersonResponseDto disabledPersonResponseDto = service.disablePerson(id);
    return ResponseEntity.ok(disabledPersonResponseDto);
  }

  @PatchMapping("/enable/{id}")
  @Override
  public ResponseEntity<PersonResponseDto> enablePerson(@PathVariable Long id) {
    PersonResponseDto enabledPersonResponseDto = service.enablePerson(id);
    return ResponseEntity.ok(enabledPersonResponseDto);
  }
}
