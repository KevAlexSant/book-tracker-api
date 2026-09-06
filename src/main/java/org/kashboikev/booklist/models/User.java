package org.kashboikev.booklist.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kashboikev.booklist.models.records.Role;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    public void addEntry(BookEntry bookEntry) {
        entries.add(bookEntry);
        bookEntry.setUser(this);

    }

    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;






    @OneToMany(mappedBy = "user")
    private List<BookEntry> entries = new ArrayList<>();


}
