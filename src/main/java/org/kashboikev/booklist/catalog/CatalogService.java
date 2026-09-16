package org.kashboikev.booklist.catalog;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.kashboikev.booklist.catalog.googlebooks.*;
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

        if (volumeInfo.description() != null) {
            String description = volumeInfo.description();
            String cleanDescription = Jsoup.parse(description).text();
            book.setDescription(cleanDescription);
        } else {
            book.setDescription("");
        }


        if (volumeInfo.industryIdentifiers() != null) {
            for (var id : volumeInfo.industryIdentifiers()) {
                if ("ISBN_10".equals(id.type())) {
                    book.setIsbn10(id.identifier());
                } else if ("ISBN_13".equals(id.type())) {
                    book.setIsbn13(id.identifier());
                }
            }
        }

        if (volumeInfo.imageLinks() != null) {
            book.setImageUrl(volumeInfo.imageLinks().thumbnail());
        }



        if (volumeInfo.authors() != null) {
            for (String author : volumeInfo.authors()) {
                book.getAuthors().add(author);
            }
        }


        bookRepository.save(book);

        return book;
    }


}
