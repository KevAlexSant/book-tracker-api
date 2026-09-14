package org.kashboikev.booklist.shelf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.account.UserNotFoundException;
import org.kashboikev.booklist.account.UserRepository;
import org.kashboikev.booklist.catolog.Book;
import org.kashboikev.booklist.catolog.CatalogService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void editRatingStatus_updatesRating_Status_whenBookEntryExists() {
        BookEntry bookEntry = new BookEntry();
        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));
        shelfService.editRatingStatus(5, "Kevin", 52);


        assertEquals(5, bookEntry.getRating());

        verify(bookEntryRepository).save(bookEntry);


    }

    @Test
    void editRatingStatus_throwsException_whenBookEntryDoesNotExist(){

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.empty());


        assertThrows(BookEntryNotFoundException.class, () -> shelfService.editRatingStatus(5, "Kevin", 52));

    }

    @Test
    void editReadingStatus_updatesReadingStatus_whenBookEntryExist() {
        BookEntry bookEntry = new BookEntry();

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));

        shelfService.editReadingStatus(ReadingStatus.READING, 52, "Kevin");

        assertEquals(ReadingStatus.READING, bookEntry.getReadingStatus());

        verify(bookEntryRepository).save(bookEntry);

    }

    @Test
    void editReadingStatus_throwsException_whenBookEntryDoesNotExist() {

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.empty());
        assertThrows(BookEntryNotFoundException.class,
                () -> shelfService.editReadingStatus(ReadingStatus.READING,
                52, "Kevin"));

    }

    @Test
    void deleteBookEntry_deletesBookEntry_whenBookEntryExists(){

        User user = new User();
        BookEntry bookEntry = new BookEntry();

        user.addEntry(bookEntry);

        when(bookEntryRepository.findByIdAndUserUsername(52, "Kevin"))
                .thenReturn(Optional.of(bookEntry));

        shelfService.deleteBookEntry(52, "Kevin");

        assertFalse(user.getEntries().contains(bookEntry));

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
                IllegalStateException.class,
                () -> shelfService.addBookEntry("Kevin", "123")
        );

        verifyNoInteractions(catalogService);
        verify(bookEntryRepository, never()).save(any());
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
        verify(bookEntryRepository, never()).save(any());
    }
}
