package org.kashboikev.booklist.catalog;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.kashboikev.booklist.catalog.googlebooks.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class CatalogService {

    private final GoogleBooksClient googleBooksClient;
    private final BookRepository bookRepository;

    public CatalogService(GoogleBooksClient googleBooksClient, BookRepository bookRepository) {
        this.googleBooksClient = googleBooksClient;
        this.bookRepository = bookRepository;
    }

    public List<BookSearchResponse> searchBooks(String query) {
        log.info("Searching books for query: {}", query);

        String normalized = query
                .replace("-", "")
                .replace(" ", "");
        log.info("Normalized query: {}", normalized);

        GoogleBooksResponse response;

        if (isIsbn(normalized)) {
            log.info("Query is ISBN");
            response =  googleBooksClient.searchBooks("isbn:" + normalized);
        } else {
            log.info("Query is not ISBN");
            response =  googleBooksClient.searchBooks(query);
        }

        List<GoogleBookItem> result = response.items() != null ? response.items() : List.of();

        return result.stream().map(BookSearchMapper::toResponse).toList();






    }

    private boolean isIsbn(String query) {
        return query.matches("\\d{13}|\\d{9}[\\dXx]");
    }


    @Transactional
    public Book getOrCreateBook(String googleBookId) {
        return bookRepository.findByGoogleBookId(googleBookId)
                .orElseGet(() -> importBookFromGoogle(googleBookId));

    }

    private Book importBookFromGoogle(String googleBookId) {
        log.info("Importing book from Google Books: {}", googleBookId);
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
