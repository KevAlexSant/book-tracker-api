package org.kashboikev.booklist.catalog;

import org.kashboikev.booklist.catalog.googlebooks.GoogleBookItem;
import org.kashboikev.booklist.catalog.googlebooks.GoogleBooksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CatalogService catalogService;

    public SearchController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }


    @GetMapping
    public ResponseEntity<List<GoogleBookItem>> searchBooks(@RequestParam(required = false) String query) {

        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        GoogleBooksResponse response = catalogService.searchBooks(query);

        List<GoogleBookItem> result = response.items() != null ? response.items() : List.of();

        return ResponseEntity.ok(result);

    }

}
