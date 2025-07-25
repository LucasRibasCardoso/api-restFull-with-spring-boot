package com.LucasRibasCardoso.api_rest_with_spring_boot.unit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.PersonMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class PersonMapperTest {

  private PersonMapper mapper;
  private Person samplePerson;
  private PersonCreateDto createDto;
  private PersonUpdateDto updateDto;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(PersonMapper.class);
    samplePerson = new Person(1L, "John", "Doe", "123.456.789-00", Gender.M);
    createDto = new PersonCreateDto("Jane", "Smith", "987.654.321-00", Gender.F);
  }

  @Test
  void shouldMapEntityToDto() {
    PersonResponseDto dto = mapper.toDto(samplePerson);
    assertNotNull(dto);
    assertEquals(samplePerson.getId(), dto.getId());
    assertEquals(samplePerson.getFirstName(), dto.getFirstName());
    assertEquals(samplePerson.getLastName(), dto.getLastName());
    assertEquals(samplePerson.getCpf(), dto.getCpf());
    assertEquals(samplePerson.getGender(), dto.getGender());
  }

  @Test
  void shouldMapCreateDtoToEntity() {
    Person entity = mapper.toEntity(createDto);
    assertNotNull(entity);
    assertNull(entity.getId(), "ID must be null when mapping from create DTO");
    assertEquals(createDto.firstName(), entity.getFirstName());
    assertEquals(createDto.lastName(), entity.getLastName());
    assertEquals(createDto.cpf(), entity.getCpf());
    assertEquals(createDto.gender(), entity.getGender());
  }

  @Test
  void shouldUpdateEntityFromDto_AllFields() {
    updateDto = new PersonUpdateDto("Alice", "Wonderland", "111.222.333-44", Gender.F);
    mapper.updateEntityFromDto(updateDto, samplePerson);
    assertEquals("Alice", samplePerson.getFirstName());
    assertEquals("Wonderland", samplePerson.getLastName());
    assertEquals("111.222.333-44", samplePerson.getCpf());
    assertEquals(Gender.F, samplePerson.getGender());
  }

  @Test
  void shouldUpdateEntityFromDto_PartialFields() {
    // only update lastName and keep others
    updateDto = new PersonUpdateDto(null, "NewLast", null, null);
    String originalFirst = samplePerson.getFirstName();
    String originalCpf = samplePerson.getCpf();
    Gender originalGender = samplePerson.getGender();

    mapper.updateEntityFromDto(updateDto, samplePerson);

    assertEquals(originalFirst, samplePerson.getFirstName());
    assertEquals("NewLast", samplePerson.getLastName());
    assertEquals(originalCpf, samplePerson.getCpf());
    assertEquals(originalGender, samplePerson.getGender());
  }

  @Test
  void shouldNotModifyEntityWhenAllDtoFieldsNull() {
    updateDto = new PersonUpdateDto(null, null, null, null);
    String originalFirst = samplePerson.getFirstName();
    String originalLast = samplePerson.getLastName();
    String originalCpf = samplePerson.getCpf();
    Gender originalGender = samplePerson.getGender();

    mapper.updateEntityFromDto(updateDto, samplePerson);

    assertEquals(originalFirst, samplePerson.getFirstName());
    assertEquals(originalLast, samplePerson.getLastName());
    assertEquals(originalCpf, samplePerson.getCpf());
    assertEquals(originalGender, samplePerson.getGender());
  }
}
