package org.kashboikev.booklist.shelf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kashboikev.booklist.catalog.Book;
import org.kashboikev.booklist.catalog.BookRepository;
import org.kashboikev.booklist.catalog.CatalogService;
import org.kashboikev.booklist.catalog.googlebooks.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private GoogleBooksClient googleBooksClient;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    CatalogService catalogService;


    @Test
    void getOrCreateBook_returnsExistingBook_whenBookAlreadyExists() {
        Book book = new Book();

        when(bookRepository.findByGoogleBookId("123"))
                .thenReturn(Optional.of(book));

        Book result = catalogService.getOrCreateBook("123");


        assertSame(book, result);
        verifyNoInteractions(googleBooksClient);


    }

    @Test
    void getOrCreateBook_returnsNewBook_whenBookDoesNotExist() {
        VolumeInfo mockVolumeInfo = new VolumeInfo("The hobbit",
                null, null, null, null, null);
        GoogleBookItem googleBooksItem = new GoogleBookItem("123", mockVolumeInfo);

        when(bookRepository.findByGoogleBookId("123"))
                .thenReturn(Optional.empty());
        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBooksItem);

        Book result = catalogService.getOrCreateBook("123");

        assertEquals("The hobbit", result.getTitle());
        assertEquals("123", result.getGoogleBookId());
        assertEquals(mockVolumeInfo.title(), result.getTitle());

        verify(bookRepository).save(result);
    }

    @Test
    void importBookFromGoogle_removesHtmlFromDescription(){
        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null,
                "The Hobbit is a fantasy novel", null, null,
                null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertEquals("The Hobbit is a fantasy novel",
                result.getDescription());
        assertEquals("The Hobbit", result.getTitle());

        verify(bookRepository).save(result);

    }

    @Test
    void importBookFromGoogle_setDescriptionToEmpty_WhenGoogleDescriptionIsMissing() {
        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null,
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertEquals("", result.getDescription());

        verify(bookRepository).save(result);

    }


    @Test
    void importBookFromGoogle_setCorrectIsbnFields_whenBothIdentifiersArePresent() {
        IndustryIdentifier isbn13 = new IndustryIdentifier("ISBN_13",
                "1234");
        IndustryIdentifier isbn10 = new IndustryIdentifier("ISBN_10",
                "12345");

        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null, null,
                List.of(isbn13, isbn10), null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertEquals("12345", result.getIsbn10());
        assertEquals("1234", result.getIsbn13());

        verify(bookRepository).save(result);

    }


    @Test
    void importBookFromGoogle_setsOnlyIsbn13_whenIsbn10IsMissing() {
        IndustryIdentifier isbn13 = new IndustryIdentifier("ISBN_13",
                "1234");

        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null, null,
                List.of(isbn13), null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertNull(result.getIsbn10());
        assertEquals("1234", result.getIsbn13());

    }

    @Test
    void importBookFromGoogle_setsAuthors_whenMultipleAuthorsGiven() {
        VolumeInfo volumeInfo = new VolumeInfo(null, List.of("Kevin", "Alex"),
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertEquals(List.of("Kevin", "Alex"), result.getAuthors());

    }

    @Test
    void importBookFromGoogle_leavesAuthorsEmpty_whenAuthorsAreMissing() {
        VolumeInfo volumeInfo = new VolumeInfo(null, null,
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertTrue(result.getAuthors().isEmpty());
    }


    @Test
    void importBookFromGoogle_setsThumbnail_whenThumbnailIsPresent() {

        ImageLinks imageLinks = new ImageLinks("small.jpg", "thumbnail.jpg");

        VolumeInfo volumeInfo = new VolumeInfo(null, null, null,
                null, imageLinks, null);
        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertEquals("thumbnail.jpg", result.getImageUrl());


    }

    @Test
    void importBookFromGoogle_setsThumbnail_whenThumbnailIsAbsent() {

        VolumeInfo volumeInfo = new VolumeInfo(null, null, null,
                null, null, null);
        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.importBookFromGoogle("123");

        assertNull(result.getImageUrl());

    }

    @Test
    void searchBook_returnGoogleBooksResponse_forGivenQuery() {
        GoogleBookItem googleBookItem = new GoogleBookItem("123", null);
        GoogleBooksResponse googleBooksResponse = new GoogleBooksResponse(List.of(googleBookItem));

        when(googleBooksClient.searchBooks("Harry Potter"))
                .thenReturn(googleBooksResponse);

        GoogleBooksResponse result = catalogService.searchBooks("Harry Potter");

        assertSame(googleBooksResponse, result);
        verify(googleBooksClient).searchBooks("Harry Potter");
    }








}
