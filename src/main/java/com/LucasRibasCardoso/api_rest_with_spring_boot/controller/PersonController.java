package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
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
  public ResponseEntity<Person> findById(@PathVariable Long id) {
    Person person = service.getById(id);
    return ResponseEntity.ok(person);
  }

  @GetMapping(
          produces = {
                  MediaType.APPLICATION_JSON_VALUE,
                  MediaType.APPLICATION_XML_VALUE,
                  MediaType.APPLICATION_YAML_VALUE
          })
  public ResponseEntity<List<Person>> findAll() {
    List<Person> persons = service.getAll();
    return ResponseEntity.ok(persons);
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
  public ResponseEntity<Person> create(@RequestBody PersonCreateDto personCreateDto) {
    Person createdPerson = service.save(personCreateDto);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdPerson.getId())
            .toUri();
    return ResponseEntity.created(location).body(createdPerson);
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
  public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody @Valid PersonUpdateDto personUpdateDto) {
    Person updatedPerson = service.update(id, personUpdateDto);
    return ResponseEntity.ok(updatedPerson);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
