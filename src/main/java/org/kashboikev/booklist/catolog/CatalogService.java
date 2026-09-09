package org.kashboikev.booklist.catolog;

import lombok.extern.slf4j.Slf4j;
import org.kashboikev.booklist.catolog.googlebooks.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CatalogService {

    private final GoogleBooksClient googleBooksClient;
    private final BookRepository bookRepository;

    public CatalogService(GoogleBooksClient googleBooksClient, BookRepository bookRepository) {
        this.googleBooksClient = googleBooksClient;
        this.bookRepository = bookRepository;
    }


    public GoogleBooksResponse searchBooks(String query) {
        return googleBooksClient.searchBooks(query);
    }

    public Book getOrCreateBook(String googleBookId) {
        return bookRepository.findByGoogleBookId(googleBookId)
                .orElseGet(() -> importBookFromGoogle(googleBookId));

    }

    public Book importBookFromGoogle(String googleBookId) {
        GoogleBookItem bookItem = googleBooksClient.getBook(googleBookId);
        VolumeInfo volumeInfo = bookItem.volumeInfo();

        Book book = new Book();
        book.setGoogleBookId(googleBookId);
        book.setTitle(volumeInfo.title());
        book.setPublishedDate(volumeInfo.publishedDate());
        book.setDescription(volumeInfo.description());
        book.setIsbn10(volumeInfo.industryIdentifiers().get(0).identifier());
        book.setIsbn13(volumeInfo.industryIdentifiers().get(1).identifier());
        book.setImageUrl(volumeInfo.imageLinks().thumbnail());

        for(String authors : volumeInfo.authors()) {
            book.getAuthors().add(authors);
        }

        bookRepository.save(book);
        log.info("Book imported from Google Books");
        return book;
    }


}
