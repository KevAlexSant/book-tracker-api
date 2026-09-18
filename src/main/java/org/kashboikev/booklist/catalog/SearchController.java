package org.kashboikev.booklist.catalog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchController {

    private final CatalogService catalogService;

    public SearchController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }


    @GetMapping
    public ResponseEntity<List<BookSearchResponse>> searchBooks(String query) {
        log.info("Searching books for query: {}", query);
        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        List<BookSearchResponse> responses = catalogService.searchBooks(query);
        return ResponseEntity.ok(responses);

    }

}
