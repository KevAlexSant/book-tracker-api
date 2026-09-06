package org.kashboikev.booklist.controllers;

import org.kashboikev.booklist.models.dto.GoogleBooksResponse;
import org.kashboikev.booklist.services.GoogleBooksService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    private final GoogleBooksService googleBooksService;

    public SearchController(GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }


    @GetMapping("/books/search")
    public String searchBooks(
            @RequestParam(required = false) String q,
            Model model) {

        if (q != null && !q.isBlank()) {

            GoogleBooksResponse response =
                    googleBooksService.searchBooks(q);

            model.addAttribute("results", response.items());
        }

        return "book-search";
    }

}
