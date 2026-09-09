package org.kashboikev.booklist.shelf;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.catolog.Book;

@Getter
@Setter
@Entity
@Table(name = "book_entries")
public class BookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ReadingStatus readingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = true)
    private Integer rating;


}
