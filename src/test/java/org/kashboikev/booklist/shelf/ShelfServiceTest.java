package org.kashboikev.booklist.shelf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.account.UserRepository;
import org.kashboikev.booklist.catalog.Book;
import org.kashboikev.booklist.catalog.CatalogService;
import org.kashboikev.booklist.exceptions.BookEntryAlreadyExistsException;
import org.kashboikev.booklist.exceptions.BookEntryNotFoundException;
import org.kashboikev.booklist.exceptions.UserNotFoundException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfServiceTest {

    @Mock
    private BookEntryRepository bookEntryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private ShelfService shelfService;

    @Test
    void getAllBookEntries_returnsMappedBookEntries_forUser() {
        Book book = new Book();
        book.setGoogleBookId("123");
        book.setTitle("The Hobbit");

        BookEntry bookEntry = new BookEntry();
        bookEntry.setBook(book);
        bookEntry.setRating(3);
        bookEntry.setReadingStatus(ReadingStatus.READING);

        when(bookEntryRepository.findAllByUserUsernameOrderByIdAsc("Kevin"))
                .thenReturn(List.of(bookEntry));

        List<ShelfEntryResponse> result = shelfService.getAllBookEntries("Kevin");

        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).title());
        assertEquals(3, result.get(0).rating());
        assertEquals("123", result.get(0).googleBookId());
        assertEquals(ReadingStatus.READING, result.get(0).readingStatus());
    }

    @Test
    void updateBookRating_updatesBookRating_whenBookEntryExists() {
        BookEntry bookEntry = new BookEntry();

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));

        shelfService.updateBookRating(3, "Kevin", 52);

        assertEquals(3, bookEntry.getRating());
    }

    @Test
    void updateBookRating_throwsException_whenBookEntryDoesNotExist() {
        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.empty());
        assertThrows(BookEntryNotFoundException.class,
                () -> shelfService.updateBookRating(3, "Kevin", 52));
    }


    @Test
    void editReadingStatus_updatesReadingStatus_whenBookEntryExist() {
        BookEntry bookEntry = new BookEntry();

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));

        shelfService.updateReadingStatus(ReadingStatus.READING, 52, "Kevin");

        assertEquals(ReadingStatus.READING, bookEntry.getReadingStatus());


    }


    @Test
    void editReadingStatus_throwsException_whenBookEntryDoesNotExist() {

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.empty());
        assertThrows(BookEntryNotFoundException.class,
                () -> shelfService.updateReadingStatus(ReadingStatus.READING,
                52, "Kevin"));

    }

    @Test
    void deleteBookEntry_deletesBookEntry_whenBookEntryExists(){
        BookEntry bookEntry = new BookEntry();


        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));

        shelfService.deleteBookEntry(52, "Kevin");

        verify(bookEntryRepository).delete(bookEntry);
    }

    @Test
    void deleteBookEntry_throwsException_whenBookEntryDoesNotExist() {
        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.empty());

        assertThrows(BookEntryNotFoundException.class,
                () -> shelfService.deleteBookEntry(52, "Kevin"));

    }


    @Test
    void addBookEntry_throwsException_whenBookEntryAlreadyExists() {
        User user = new User();

        when(userRepository.findByUsername("Kevin"))
                .thenReturn(Optional.of(user));
        when(bookEntryRepository
                .existsByUser_UsernameAndBook_GoogleBookId("Kevin", "123"))
                .thenReturn(true);

        assertThrows(
                BookEntryAlreadyExistsException.class,
                () -> shelfService.addBookEntry("Kevin", "123")
        );

        verifyNoInteractions(catalogService);
    }


    @Test
    void addBookEntry_createsBookEntry_whenBookEntryDoesNotExist() {
        Book book = new Book();
        User user = new User();

        when(bookEntryRepository.existsByUser_UsernameAndBook_GoogleBookId("Kevin", "123"))
                .thenReturn(false);
        when(catalogService.getOrCreateBook("123")).thenReturn(book);
        when(userRepository.findByUsername("Kevin")).thenReturn(Optional.of(user));

        shelfService.addBookEntry("Kevin", "123");

        ArgumentCaptor<BookEntry> bookEntryCaptor = ArgumentCaptor.forClass(BookEntry.class);
        verify(bookEntryRepository).save(bookEntryCaptor.capture());
        BookEntry bookEntry = bookEntryCaptor.getValue();
        assertSame(book, bookEntry.getBook());
        assertSame(user, bookEntry.getUser());
    }



    @Test
    void addBookEntry_throwsException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("Kevin"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shelfService.addBookEntry("Kevin", "123"));


        verifyNoInteractions(catalogService);
    }

}
