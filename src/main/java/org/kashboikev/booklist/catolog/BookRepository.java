package org.kashboikev.booklist.catolog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByGoogleBookId(String googleBookId);




}
