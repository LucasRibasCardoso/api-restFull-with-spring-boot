package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.PersonMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @InjectMocks private PersonService service;

  @Mock private PersonRepository repository;

  @Mock private PersonMapper personMapper;

  @Test
  void getById() {
    // Arrange
    Person personMock = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);
    when(repository.findById(1L)).thenReturn(Optional.of(personMock));

    PersonResponseDto dtoMock =
        new PersonResponseDto(1L, "John", "Doe", "123.456.789-00", Gender.M);
    when(personMapper.toDto(personMock)).thenReturn(dtoMock);

    // Act
    PersonResponseDto result = service.getById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("delete")));

    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123.456.789-00", result.getCpf());
    assertEquals(Gender.M, result.getGender());
  }

  @Test
  void getAll() {
    // Arrange
    Person p1 = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);
    Person p2 = new Person(2L, "Jane", "Doe", "321.654.987.00", Gender.F);
    when(repository.findAll()).thenReturn(List.of(p1, p2));
    PersonResponseDto dto1 =
        new PersonResponseDto(1L, p1.getFirstName(), p1.getLastName(), p1.getCpf(), p1.getGender());
    PersonResponseDto dto2 =
        new PersonResponseDto(2L, p2.getFirstName(), p2.getLastName(), p2.getCpf(), p2.getGender());
    when(personMapper.toDto(p1)).thenReturn(dto1);
    when(personMapper.toDto(p2)).thenReturn(dto2);

    // Act
    List<PersonResponseDto> result = service.getAll();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());

    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("delete")));

    assertEquals("John", result.getFirst().getFirstName());
    assertEquals("Doe", result.getFirst().getLastName());
    assertEquals("123.456.789-00", result.getFirst().getCpf());
    assertEquals(Gender.M, result.getFirst().getGender());
  }

  @Test
  void save() {
    // Arrange
    PersonCreateDto createDto = new PersonCreateDto("John", "Doe", "123.456.789-00", Gender.M);
    Person entityPerson = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);
    PersonResponseDto responseDto =
        new PersonResponseDto(1L, "John", "Doe", "123.456.789-00", Gender.M);

    when(personMapper.toEntity(createDto)).thenReturn(entityPerson);
    when(personMapper.toDto(entityPerson)).thenReturn(responseDto);
    when(repository.save(entityPerson)).thenReturn(entityPerson);

    // Act
    PersonResponseDto result = service.save(createDto);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("delete")));

    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123.456.789-00", result.getCpf());
    assertEquals(Gender.M, result.getGender());
  }

  @Test
  void saveWithNullDto() {
    Exception exception =
        assertThrows(
            RequiredObjectIsNullException.class,
            () -> {
              service.save(null);
            });

    String expectedMessage = "It is not allowed to persist a null object.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void savePersonAlreadyExists() {
    // Arrange
    PersonCreateDto createDto = new PersonCreateDto("John", "Doe", "123.456.789-00", Gender.M);
    when(repository.existsByCpf(createDto.cpf())).thenReturn(true);

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonAlreadyExistsException.class,
            () -> {
              service.save(createDto);
            });

    String expectedMessage = "This CPF is already in use";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void update() {
    // Arrange
    PersonUpdateDto updateDto = new PersonUpdateDto("Julios", null, null, null);
    Person entityPerson = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);
    PersonResponseDto responseDto =
        new PersonResponseDto(1L, "Julios", "Doe", "123.456.789-00", Gender.M);

    when(repository.findById(1L)).thenReturn(Optional.of(entityPerson));
    doNothing().when(personMapper).updateEntityFromDto(updateDto, entityPerson);
    when(repository.save(entityPerson)).thenReturn(entityPerson);
    when(personMapper.toDto(entityPerson)).thenReturn(responseDto);

    // Act
    PersonResponseDto result = service.update(1L, updateDto);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/person")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/person/1")
                        && link.getType().equals("delete")));

    assertEquals("Julios", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("123.456.789-00", result.getCpf());
    assertEquals(Gender.M, result.getGender());
  }

  @Test
  void updateWithNullDto() {
    Exception exception =
        assertThrows(
            RequiredObjectIsNullException.class,
            () -> {
              service.update(1L, null);
            });

    String expectedMessage = "It is not allowed to persist a null object.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void updatePersonNotFound() {
    // Arrange
    PersonUpdateDto updateDto = new PersonUpdateDto("Julios", null, null, null);
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonNotFoundException.class,
            () -> {
              service.update(1L, updateDto);
            });

    String expectedMessage = "Person not found with id: 1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void delete() {
    // Arrange
    Person entityPerson = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);

    when(repository.findById(1L)).thenReturn(Optional.of(entityPerson));

    // Act
    service.delete(1L);

    // Assert
    verify(repository, times(1)).delete(entityPerson);
    verify(repository, times(1)).findById(1L);
  }

  @Test
  void deletePersonNotFound() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonNotFoundException.class,
            () -> {
              service.delete(1L);
            });

    String expectedMessage = "Person not found with id: 1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
