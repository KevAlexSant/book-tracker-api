package org.kashboikev.booklist.catolog;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String googleBookId;
    @Column(nullable = true)
    private String imageUrl;
    @ElementCollection
    @CollectionTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id")
    )
    @Column(name = "author")
    private List<String> authors = new ArrayList<>();
}
