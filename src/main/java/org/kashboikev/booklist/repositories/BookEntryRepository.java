package org.kashboikev.booklist.repositories;

import org.kashboikev.booklist.models.Book;
import org.kashboikev.booklist.models.BookEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntryRepository extends JpaRepository<BookEntry, Integer> {

}
