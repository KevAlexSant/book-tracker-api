package org.kashboikev.booklist.shelf;


import lombok.extern.slf4j.Slf4j;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.account.UserNotFoundException;
import org.kashboikev.booklist.account.UserRepository;
import org.kashboikev.booklist.catolog.Book;
import org.kashboikev.booklist.catolog.CatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShelfService {
    private final BookEntryRepository bookEntryRepository;
    private final UserRepository userRepository;
    private final CatalogService catalogService;


    public ShelfService(BookEntryRepository bookEntryRepository, UserRepository userRepository,
                        CatalogService catalogService) {
        this.bookEntryRepository = bookEntryRepository;
        this.userRepository = userRepository;
        this.catalogService = catalogService;
    }


    public List<BookEntry> getAllBookEntries(String username) {
        return bookEntryRepository.findAllByUserUsernameOrderByIdAsc(username);
    }

    public void addBookEntry(String username, String googleBookId) {

        if (bookEntryRepository
                .existsByUser_UsernameAndBook_GoogleBookId(username, googleBookId)) {
            throw new IllegalStateException("Book already exists");
        } else {
            createBookEntry(googleBookId, username);

        }


    }


    public BookEntry createBookEntry(String googleBookId, String username) {

        Book book = catalogService.getOrCreateBook(googleBookId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));

        BookEntry bookEntry = new BookEntry();
        bookEntry.setBook(book);
        bookEntry.setUser(user);
        bookEntryRepository.save(bookEntry);
        return bookEntry;
    }


    public void editRating(Integer rating, String username, Integer bookEntryId) {
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(bookEntryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        bookEntry.setRating(rating);
        bookEntryRepository.save(bookEntry);

    }

    public void editReadingStatus(ReadingStatus readingStatus, Integer bookEntryId, String username) {
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(bookEntryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        bookEntry.setReadingStatus(readingStatus);
        bookEntryRepository.save(bookEntry);
    }

    public void deleteBookEntry(Integer entryId, String username) {
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(entryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        User user = bookEntry.getUser();
        user.getEntries().remove(bookEntry);
        bookEntryRepository.delete(bookEntry);
    }


}
