package org.kashboikev.booklist.repositories;

import org.kashboikev.booklist.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
