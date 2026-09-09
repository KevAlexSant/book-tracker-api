package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.Book;
import org.kashboikev.booklist.models.BookEntry;
import org.kashboikev.booklist.models.User;
import org.kashboikev.booklist.models.dto.GoogleBookItem;
import org.kashboikev.booklist.repositories.BookEntryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookEntryService {

    private final BookEntryRepository bookEntryRepository;
    private final UserService userService;
    private final GoogleBooksService googleBooksService;
    private final BookService bookService;

    public BookEntryService(BookEntryRepository bookEntryRepository,
                            UserService userService,
                            GoogleBooksService googleBooksService,
                            BookService bookService) {
        this.bookEntryRepository = bookEntryRepository;
        this.userService = userService;
        this.googleBooksService = googleBooksService;
        this.bookService = bookService;
    }

    public List<BookEntry> getAllBookEntries(User user) {
        return bookEntryRepository.findAllByUser(user);

    }

    public void deleteBookEntry(BookEntry bookEntry) {
        bookEntryRepository.delete(bookEntry);
    }

    public void save(BookEntry bookEntry) {
        bookEntryRepository.save(bookEntry);
    }

    public void createBookEntryForUser(String googleBookId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Book book = bookService.getGoogleBook(googleBookId);

        BookEntry bookEntry = new BookEntry();
        bookEntry.setUser(user);
        bookEntry.setBook(book);
        bookEntryRepository.save(bookEntry);

    }





}
