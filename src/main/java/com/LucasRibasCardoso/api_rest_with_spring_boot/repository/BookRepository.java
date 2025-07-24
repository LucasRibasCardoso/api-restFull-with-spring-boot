package com.LucasRibasCardoso.api_rest_with_spring_boot.repository;

import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByAuthorIgnoreCase(String author);
    boolean existsByTitleIgnoreCase(String title);
}
