package org.kashboikev.booklist.shelf;


import lombok.extern.slf4j.Slf4j;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.exceptions.BookEntryAlreadyExistsException;
import org.kashboikev.booklist.exceptions.BookEntryNotFoundException;
import org.kashboikev.booklist.exceptions.InvalidRatingValueException;
import org.kashboikev.booklist.exceptions.UserNotFoundException;
import org.kashboikev.booklist.account.UserRepository;
import org.kashboikev.booklist.catalog.Book;
import org.kashboikev.booklist.catalog.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(readOnly = true)
    public List<ShelfEntryResponse> getAllBookEntries(String username) {
        log.info("Getting all book entries for user: {}", username);
        List<BookEntry> bookEntries = bookEntryRepository.findAllByUserUsernameOrderByIdAsc(username);

        return bookEntries.stream().map(ShelfEntryMapper::toResponse).toList();

    }

    public void addBookEntry(String username, String googleBookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if (bookEntryRepository
                .existsByUser_UsernameAndBook_GoogleBookId(username, googleBookId)) {
            throw new BookEntryAlreadyExistsException("Book entry already exists");
        }

        createBookEntry(googleBookId, user);

    }


    public BookEntry createBookEntry(String googleBookId, User user) {

        Book book = catalogService.getOrCreateBook(googleBookId);

        BookEntry bookEntry = new BookEntry();
        bookEntry.setBook(book);
        bookEntry.setUser(user);
        bookEntryRepository.save(bookEntry);
        return bookEntry;
    }


    public void updateBookRating(Integer rating, String username, Integer bookEntryId) {
        if(rating != null && (rating > 5 || rating < 1)){
            throw new InvalidRatingValueException("Invalid rating value");
        }
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(bookEntryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        bookEntry.setRating(rating);
        bookEntryRepository.save(bookEntry);

    }

    public void updateReadingStatus(ReadingStatus readingStatus, Integer bookEntryId, String username) {
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(bookEntryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        bookEntry.setReadingStatus(readingStatus);
        bookEntryRepository.save(bookEntry);
    }

    public void deleteBookEntry(Integer entryId, String username) {
        BookEntry bookEntry = bookEntryRepository.findByIdAndUserUsername(entryId, username)
                .orElseThrow(() -> new BookEntryNotFoundException("Entry not found"));

        bookEntryRepository.delete(bookEntry);
    }


}
