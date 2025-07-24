package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.PersonControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "People", description = "Endpoints for managing people")
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
  public ResponseEntity<List<PersonResponseDto>> findAll() {
    List<PersonResponseDto> listOfPersonResponseDto = service.findAll();
    return ResponseEntity.ok(listOfPersonResponseDto);
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
}
