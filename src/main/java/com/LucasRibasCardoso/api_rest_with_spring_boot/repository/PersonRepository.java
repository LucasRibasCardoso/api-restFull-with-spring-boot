package com.LucasRibasCardoso.api_rest_with_spring_boot.repository;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Person;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  boolean existsByCpf(String cpf);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
  void disablePerson(@Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Person p SET p.enabled = true WHERE p.id = :id")
  void enablePerson(@Param("id") Long id);

  Page<Person> findByFirstNameContainingIgnoreCase(
      @Param("firstName") String firstName, Pageable pageable);

  List<Person> findAllByCpfIn(Collection<String> cpfs);
}
