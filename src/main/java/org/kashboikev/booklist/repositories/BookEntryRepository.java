package org.kashboikev.booklist.repositories;


import org.kashboikev.booklist.models.BookEntry;
import org.kashboikev.booklist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookEntryRepository extends JpaRepository<BookEntry, Integer> {

    List<BookEntry> findAllByUser(User user);

}
