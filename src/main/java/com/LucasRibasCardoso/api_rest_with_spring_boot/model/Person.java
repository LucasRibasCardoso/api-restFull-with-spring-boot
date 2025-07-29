package com.LucasRibasCardoso.api_rest_with_spring_boot.model;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TB_PERSON")
public class Person implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @Column(nullable = false, length = 100, unique = true)
  private String cpf;

  @Column(nullable = false, length = 6)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(nullable = false)
  private Boolean enabled;

  public Person() {}

  public Person(
      Long id, String firstName, String lastName, String cpf, Gender gender, Boolean enabled) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cpf = cpf;
    this.gender = gender;
    this.enabled = enabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Person person)) return false;
    return Objects.equals(id, person.id) && Objects.equals(cpf, person.cpf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cpf);
  }
}
