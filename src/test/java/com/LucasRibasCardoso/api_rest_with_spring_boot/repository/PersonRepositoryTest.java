package com.LucasRibasCardoso.api_rest_with_spring_boot.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

  @Autowired private PersonRepository repository;

  @Test
  @Order(1)
  void findByFirstNameContainingIgnoreCase() {
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
    Person person =
        repository.findByFirstNameContainingIgnoreCase("Lucas", pageable).getContent().get(0);

    assertNotNull(person);
    assertNotNull(person.getId());
    assertTrue(person.getId() > 0);
    assertEquals("Lucas", person.getFirstName());
    assertEquals("Cardoso", person.getLastName());
    assertEquals("123.456.789-01", person.getCpf());
    assertEquals(Gender.M, person.getGender());
    assertTrue(person.getEnabled());
  }

  @Test
  @Order(2)
  void disablePerson() {
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
    Person person =
        repository.findByFirstNameContainingIgnoreCase("Lucas", pageable).getContent().get(0);
    Long id = person.getId();
    assertTrue(person.getEnabled());
    repository.disablePerson(id);

    Person updated = repository.findById(id).orElseThrow();

    assertFalse(updated.getEnabled());
  }

  @Test
  @Order(3)
  void enablePerson() {
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
    Person person =
        repository.findByFirstNameContainingIgnoreCase("Lucas", pageable).getContent().get(0);
    Long id = person.getId();
    // ensure disabled first
    repository.disablePerson(id);
    Person disabled = repository.findById(id).orElseThrow();
    assertFalse(disabled.getEnabled());
    // now enable
    repository.enablePerson(id);
    Person enabled = repository.findById(id).orElseThrow();

    assertTrue(enabled.getEnabled());
  }

  @Test
  @Order(4)
  void existsByCpf() {
    String cpf = "123.456.789-01";
    boolean exists = repository.existsByCpf(cpf);
    assertTrue(exists);
  }

  @Test
  @Order(5)
  void existsByCpfNotFound() {
    String cpf = "987.654.321-99"; // Assuming this CPF does not exist in the database
    boolean exists = repository.existsByCpf(cpf);
    assertFalse(exists);
  }
}
