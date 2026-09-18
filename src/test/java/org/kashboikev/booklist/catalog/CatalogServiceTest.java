package org.kashboikev.booklist.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void getOrCreateBook_removesHtmlFromDescription() {
        VolumeInfo mockVolumeInfo = new VolumeInfo("The hobbit",
                null, "<p>The hobbit is a fantasy novel</p>", null, null, null);
        GoogleBookItem googleBooksItem = new GoogleBookItem("123", mockVolumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBooksItem);

        when(bookRepository.findByGoogleBookId("123"))
                .thenReturn(Optional.empty());

        Book result = catalogService.getOrCreateBook("123");

        assertEquals("The hobbit is a fantasy novel", result.getDescription());
        verify(bookRepository).save(result);
    }

    @Test
    void getOrCreateBook_setDescriptionToEmpty_WhenGoogleDescriptionIsMissing() {
        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null,
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertEquals("", result.getDescription());

        verify(bookRepository).save(result);
    }



    @Test
    void getOrCreateBook_setCorrectIsbnFields_whenBothIdentifiersArePresent() {
        IndustryIdentifier isbn13 = new IndustryIdentifier("ISBN_13",
                "1234");
        IndustryIdentifier isbn10 = new IndustryIdentifier("ISBN_10",
                "12345");

        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null, null,
                List.of(isbn13, isbn10), null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertEquals("12345", result.getIsbn10());
        assertEquals("1234", result.getIsbn13());

        verify(bookRepository).save(result);

    }


    @Test
    void getOrCreateBook_setsOnlyIsbn13_whenIsbn10IsMissing() {
        IndustryIdentifier isbn13 = new IndustryIdentifier("ISBN_13",
                "1234");

        VolumeInfo volumeInfo = new VolumeInfo("The Hobbit", null, null,
                List.of(isbn13), null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertNull(result.getIsbn10());
        assertEquals("1234", result.getIsbn13());

    }

    @Test
    void getOrCreateBook_setsAuthors_whenMultipleAuthorsGiven() {
        VolumeInfo volumeInfo = new VolumeInfo(null, List.of("Kevin", "Alex"),
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertEquals(List.of("Kevin", "Alex"), result.getAuthors());

    }

    @Test
    void getOrCreateBook_leavesAuthorsEmpty_whenAuthorsAreMissing() {
        VolumeInfo volumeInfo = new VolumeInfo(null, null,
                null, null, null, null);

        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertTrue(result.getAuthors().isEmpty());
    }


    @Test
    void getOrCreateBook_setsThumbnail_whenThumbnailIsPresent() {

        ImageLinks imageLinks = new ImageLinks("small.jpg", "thumbnail.jpg");

        VolumeInfo volumeInfo = new VolumeInfo(null, null, null,
                null, imageLinks, null);
        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertEquals("thumbnail.jpg", result.getImageUrl());


    }

    @Test
    void getOrCreateBook_setsThumbnail_whenThumbnailIsAbsent() {

        VolumeInfo volumeInfo = new VolumeInfo(null, null, null,
                null, null, null);
        GoogleBookItem googleBookItem = new GoogleBookItem("123", volumeInfo);

        when(googleBooksClient.getBook("123"))
                .thenReturn(googleBookItem);

        Book result = catalogService.getOrCreateBook("123");

        assertNull(result.getImageUrl());

    }

    @Test
    void searchBooks_returnsMappedBookSearchResponses_forGivenQuery() {
        VolumeInfo volumeInfo = new VolumeInfo(
                "Harry Potter",
                List.of("J.K. Rowling"),
                null,
                null,
                null,
                "1997"
        );

        GoogleBookItem googleBookItem =
                new GoogleBookItem("123", volumeInfo);

        GoogleBooksResponse googleBooksResponse =
                new GoogleBooksResponse(List.of(googleBookItem));

        when(googleBooksClient.searchBooks("Harry Potter"))
                .thenReturn(googleBooksResponse);

        List<BookSearchResponse> result =
                catalogService.searchBooks("Harry Potter");

        assertEquals(1, result.size());

        BookSearchResponse response = result.get(0);

        assertEquals("123", response.googleBookId());
        assertEquals("Harry Potter", response.title());
        assertEquals(List.of("J.K. Rowling"), response.authors());
        assertEquals("1997", response.publishedDate());

        verify(googleBooksClient).searchBooks("Harry Potter");
    }


}
