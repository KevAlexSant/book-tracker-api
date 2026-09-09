package org.kashboikev.booklist.shelf;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shelf")
public class ShelfController {


    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping()
    public String getShelf(Model model, Authentication authentication) {

        String username = authentication.getName();

        List<BookEntry> bookEntries = shelfService.getAllBookEntries(username);

        model.addAttribute("bookEntries", bookEntries);

        return "shelf";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam String googleBookId, Authentication authentication) {
        String username = authentication.getName();
        shelfService.addBookEntry(username, googleBookId);

        return "redirect:/shelf";

    }

    @PostMapping("/entries/{entryId}/rating")
    public String addRating(@RequestParam(required = false) int rating, @PathVariable Long entryId, Authentication authentication) {

        shelfService.EditRating(rating, authentication.getName(), entryId);

        return "redirect:/shelf";
    }
}