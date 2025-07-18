package com.LucasRibasCardoso.api_rest_with_spring_boot.repository;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  boolean existsByCpf(String cpf);
}
