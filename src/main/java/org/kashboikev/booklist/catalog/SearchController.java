package org.kashboikev.booklist.catalog;

import org.kashboikev.booklist.catalog.googlebooks.GoogleBooksResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class SearchController {

    private final CatalogService catalogService;

    public SearchController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/books/search")
    public String searchBooks(
            @RequestParam(required = false) String query,
            Model model) {

        if (query == null || query.isBlank()) {
            model.addAttribute("results", List.of());
            return "book-search";
        }

        GoogleBooksResponse response =
                catalogService.searchBooks(query);

        model.addAttribute("results", response.items());

        return "book-search";
    }

}
