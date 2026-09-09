package org.kashboikev.booklist.controllers;

import org.kashboikev.booklist.models.Book;
import org.kashboikev.booklist.models.BookEntry;
import org.kashboikev.booklist.models.User;
import org.kashboikev.booklist.models.dto.GoogleBookItem;
import org.kashboikev.booklist.models.dto.GoogleBooksResponse;
import org.kashboikev.booklist.repositories.BookEntryRepository;
import org.kashboikev.booklist.repositories.BookRepository;
import org.kashboikev.booklist.services.BookEntryService;
import org.kashboikev.booklist.services.GoogleBooksService;
import org.kashboikev.booklist.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shelf")
public class ShelfController {

    private final BookEntryService bookEntryService;
    private final UserService userService;
    private final GoogleBooksService googleBooksService;
    private final BookEntryRepository bookEntryRepository;
    private final BookRepository bookRepository;

    public ShelfController(BookEntryService bookEntryService,
                           UserService userService,
                           GoogleBooksService googleBooksService, BookEntryRepository bookEntryRepository, BookRepository bookRepository) {
        this.bookEntryService = bookEntryService;
        this.userService = userService;
        this.googleBooksService = googleBooksService;
        this.bookEntryRepository = bookEntryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping()
    public String showShelf(Authentication authentication, Model model) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        List<BookEntry> bookEntries = bookEntryService.getAllBookEntries(user);

        model.addAttribute("bookEntries", bookEntries);

        return "shelf";
    }

    @PostMapping("/add")
    public String addtoShelf(@RequestParam String googleBookId, Authentication authentication) {

        bookEntryService.createBookEntryForUser(googleBookId, authentication);

        return "redirect:/shelf";
    }
}
