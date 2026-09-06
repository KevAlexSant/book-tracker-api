package org.kashboikev.booklist.controllers;

import org.kashboikev.booklist.models.BookEntry;
import org.kashboikev.booklist.models.User;
import org.kashboikev.booklist.services.BookEntryService;
import org.kashboikev.booklist.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shelf")
public class ShelfController {

    private final BookEntryService bookEntryService;
    private final UserService userService;

    public ShelfController(BookEntryService bookEntryService,
                           UserService userService) {
        this.bookEntryService = bookEntryService;
        this.userService = userService;
    }

    @GetMapping("/shelf")
    public String showShelf(Authentication authentication, Model model) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        List<BookEntry> bookEntries = bookEntryService.getAllBookEntries(user);

        model.addAttribute("bookEntries", bookEntries);

        return "shelf";
    }


}
