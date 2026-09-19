package org.kashboikev.booklist.shelf;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.kashboikev.booklist.account.User;
import org.kashboikev.booklist.catalog.Book;

@Getter
@Setter
@Entity
@Table(name = "book_entries", uniqueConstraints = { @UniqueConstraint(
                name = "uk_user_book",
                columnNames = {"user_id", "book_id"}
                )
        }
)
public class BookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_entry_seq_generator")
    @SequenceGenerator(name = "book_entry_seq_generator", sequenceName = "book_entries_seq", allocationSize = 1)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus readingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Min(1)
    @Max(5)
    @Column(name = "rating", nullable = true)
    private Integer rating;


}
