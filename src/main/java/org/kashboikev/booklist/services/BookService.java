package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.Author;
import org.kashboikev.booklist.models.Book;
import org.kashboikev.booklist.models.dto.GoogleBookItem;
import org.kashboikev.booklist.models.dto.IndustryIdentifier;
import org.kashboikev.booklist.models.dto.VolumeInfo;
import org.kashboikev.booklist.repositories.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final GoogleBooksService googleBooksService;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, GoogleBooksService googleBooksService,
                       AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.googleBooksService = googleBooksService;
        this.authorService = authorService;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getGoogleBook(String id) {
        GoogleBookItem googleBookItem = googleBooksService.getBook(id);
        VolumeInfo volumeInfo = googleBookItem.volumeInfo();
        Book book = new Book();
        book.setGoogleVolumeId(googleBookItem.id());
        book.setTitle(volumeInfo.title());

        List<Author> authors = authorService.getOrCreateAuthors(volumeInfo);
        for(Author author : authors) {
            book.addAuthor(author);
        }

        book.setDescription(volumeInfo.description());

        for (IndustryIdentifier identifier : volumeInfo.industryIdentifiers()) {

            if ("ISBN_10".equals(identifier.type())) {
                book.setIsbn10(identifier.identifier());
            }

            if ("ISBN_13".equals(identifier.type())) {
                book.setIsbn13(identifier.identifier());
            }
        }
        book.setPublishedDate(volumeInfo.publishedDate());
        book.setImageUrl(volumeInfo.imageLinks().thumbnail());
        bookRepository.save(book);

        return book;


    }

}
