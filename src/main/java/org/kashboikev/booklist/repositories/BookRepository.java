package org.kashboikev.booklist.repositories;

import org.kashboikev.booklist.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByIsbn13(String isbn13);

}
