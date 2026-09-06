package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.BookEntry;
import org.kashboikev.booklist.models.User;
import org.kashboikev.booklist.repositories.BookEntryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookEntryService {

    private final BookEntryRepository bookEntryRepository;

    public BookEntryService(BookEntryRepository bookEntryRepository) {
        this.bookEntryRepository = bookEntryRepository;
    }

    public List<BookEntry> getAllBookEntries(User user) {
        return bookEntryRepository.findAllByUser(user);

    }


}
