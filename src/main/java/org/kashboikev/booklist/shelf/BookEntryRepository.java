package org.kashboikev.booklist.shelf;



import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookEntryRepository extends JpaRepository<BookEntry, Integer> {

    boolean existsByUser_UsernameAndBook_GoogleBookId(String username, String googleBookId);

    Optional<BookEntry> findByIdAndUserUsername(Integer id, String username);

    List<BookEntry> findAllByUserUsernameOrderByIdAsc(String username);




}
