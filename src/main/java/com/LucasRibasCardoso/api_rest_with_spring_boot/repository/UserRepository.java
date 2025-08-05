package com.LucasRibasCardoso.api_rest_with_spring_boot.repository;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  boolean existsByPhone(String phone);
}
