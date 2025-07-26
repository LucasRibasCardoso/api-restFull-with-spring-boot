package com.LucasRibasCardoso.api_rest_with_spring_boot.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.PersonMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.PersonRepository;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.PersonService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @InjectMocks private PersonService service;

  @Mock private PersonRepository repository;

  @Mock private PersonMapper personMapper;

  private Person personEntity;
  private PersonResponseDto personResponseDto;
  private PersonCreateDto personCreateDto;

  @BeforeEach
  void setUp() {
    personEntity = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.TRUE);
    personResponseDto =
        new PersonResponseDto(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.TRUE);
    personCreateDto = new PersonCreateDto("John", "Doe", "123.456.789-00", Gender.M);
  }

  @Test
  void findById() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.of(personEntity));
    when(personMapper.toDto(personEntity)).thenReturn(personResponseDto);

    // Act
    PersonResponseDto result = service.findById(1L);

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

    assertEquals(personEntity.getFirstName(), result.getFirstName());
    assertEquals(personEntity.getLastName(), result.getLastName());
    assertEquals(personEntity.getCpf(), result.getCpf());
    assertEquals(personEntity.getGender(), result.getGender());
  }

  @Test
  void findAll() {
    // Arrange
    when(repository.findAll()).thenReturn(List.of(personEntity));
    when(personMapper.toDto(personEntity)).thenReturn(personResponseDto);

    // Act
    List<PersonResponseDto> result = service.findAll();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());

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

    assertEquals(personEntity.getFirstName(), result.getFirst().getFirstName());
    assertEquals(personEntity.getLastName(), result.getFirst().getLastName());
    assertEquals(personEntity.getCpf(), result.getFirst().getCpf());
    assertEquals(personEntity.getGender(), result.getFirst().getGender());
  }

  @Test
  void save() {
    // Arrange
    when(personMapper.toEntity(personCreateDto)).thenReturn(personEntity);
    when(personMapper.toDto(personEntity)).thenReturn(personResponseDto);
    when(repository.save(personEntity)).thenReturn(personEntity);

    // Act
    PersonResponseDto result = service.save(personCreateDto);

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

    assertEquals(personEntity.getFirstName(), result.getFirstName());
    assertEquals(personEntity.getLastName(), result.getLastName());
    assertEquals(personEntity.getCpf(), result.getCpf());
    assertEquals(personEntity.getGender(), result.getGender());
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
    when(repository.existsByCpf(personCreateDto.cpf())).thenReturn(true);

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonAlreadyExistsException.class,
            () -> {
              service.save(personCreateDto);
            });

    String expectedMessage = "This CPF is already in use";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void update() {
    // Arrange
    PersonUpdateDto updateDto = new PersonUpdateDto("Julios", null, null, null);
    when(repository.findById(1L)).thenReturn(Optional.of(personEntity));
    doNothing().when(personMapper).updateEntityFromDto(updateDto, personEntity);
    when(repository.save(personEntity)).thenReturn(personEntity);
    when(personMapper.toDto(personEntity)).thenReturn(personResponseDto);

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

    assertEquals(personEntity.getFirstName(), result.getFirstName());
    assertEquals(personEntity.getLastName(), result.getLastName());
    assertEquals(personEntity.getCpf(), result.getCpf());
    assertEquals(personEntity.getGender(), result.getGender());
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
    when(repository.findById(1L)).thenReturn(Optional.of(personEntity));

    // Act
    service.delete(1L);

    // Assert
    verify(repository, times(1)).delete(personEntity);
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

  @Test
  void disablePerson() {
    // Arrange
    Person disabledPerson =
        new Person(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.FALSE);
    PersonResponseDto disabledPersonResponse =
        new PersonResponseDto(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.FALSE);
    when(repository.findById(1L)).thenReturn(Optional.of(personEntity));
    doNothing().when(repository).disablePerson(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(disabledPerson));
    when(personMapper.toDto(disabledPerson)).thenReturn(disabledPersonResponse);

    PersonResponseDto result = service.disablePerson(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertFalse(result.getEnabled());
    verify(repository, times(2)).findById(1L);
    verify(repository, times(1)).disablePerson(1L);
    verify(personMapper, times(1)).toDto(disabledPerson);
  }

  @Test
  void disablePersonNotFound() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonNotFoundException.class,
            () -> {
              service.disablePerson(1L);
            });

    String expectedMessage = "Person not found with id: 1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void enablePerson() {
    // Arrange
    Person enabledPerson = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.TRUE);
    PersonResponseDto enabledPersonResponse =
        new PersonResponseDto(1L, "John", "Doe", "123.456.789-00", Gender.M, Boolean.TRUE);
    when(repository.findById(1L)).thenReturn(Optional.of(personEntity));
    doNothing().when(repository).enablePerson(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(enabledPerson));
    when(personMapper.toDto(enabledPerson)).thenReturn(enabledPersonResponse);

    PersonResponseDto result = service.enablePerson(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertTrue(result.getEnabled());
    verify(repository, times(2)).findById(1L);
    verify(repository, times(1)).enablePerson(1L);
    verify(personMapper, times(1)).toDto(enabledPerson);
  }

  @Test
  void enablePersonNotFound() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            PersonNotFoundException.class,
            () -> {
              service.enablePerson(1L);
            });

    String expectedMessage = "Person not found with id: 1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
