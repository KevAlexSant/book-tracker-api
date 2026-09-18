package org.kashboikev.booklist.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kashboikev.booklist.shelf.BookEntry;
import org.kashboikev.booklist.securityconfig.Role;
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

    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



}
