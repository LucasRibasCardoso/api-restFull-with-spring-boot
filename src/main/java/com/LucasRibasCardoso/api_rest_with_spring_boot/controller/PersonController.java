package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.PersonService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

  @Autowired private PersonService service;

  @GetMapping(
      value = "/{id}",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      })
  public ResponseEntity<PersonResponseDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @GetMapping(
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      })
  public ResponseEntity<List<PersonResponseDto>> findAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @PostMapping(
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      },
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      })
  public ResponseEntity<PersonResponseDto> create(@RequestBody PersonCreateDto personCreateDto) {
    PersonResponseDto dto = service.save(personCreateDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();
    return ResponseEntity.created(location).body(dto);
  }

  @PatchMapping(
      value = "/{id}",
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      },
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_YAML_VALUE
      })
  public ResponseEntity<PersonResponseDto> update(
      @PathVariable Long id, @RequestBody @Valid PersonUpdateDto personUpdateDto) {
    return ResponseEntity.ok(service.update(id, personUpdateDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
