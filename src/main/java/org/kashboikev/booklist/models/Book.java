package org.kashboikev.booklist.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    private String publishedDate;
    @Column(length = 10000)
    private String description;
    @Column(unique = true, nullable = true)
    private String isbn10;
    @Column(unique = true, nullable = true)
    private String isbn13;
    @Column(unique = true, nullable = true)
    private String googleVolumeId;
    @Column(nullable = true)
    private String imageUrl;

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();



}
