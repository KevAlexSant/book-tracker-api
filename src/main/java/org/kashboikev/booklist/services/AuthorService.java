package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.Author;
import org.kashboikev.booklist.models.dto.VolumeInfo;
import org.kashboikev.booklist.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public void save(Author author) {
        authorRepository.save(author);
    }

    public List<Author> getOrCreateAuthors(VolumeInfo volumeInfo) {
        List<Author> authors = new ArrayList<>();

        for (String authorName : volumeInfo.authors()) {

            Author author = authorRepository
                    .findByName(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorName);
                        return authorRepository.save(newAuthor);
                    });

            authors.add(author);
        }

        return authors;
    }





}
