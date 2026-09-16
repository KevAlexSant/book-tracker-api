package org.kashboikev.booklist.shelf;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/shelf")
public class ShelfController {


    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping()
    public ResponseEntity<List<ShelfEntryResponse>> getShelf(Authentication authentication) {
        String username = authentication.getName();

        List<BookEntry> bookEntries = shelfService.getAllBookEntries(username);

        List<ShelfEntryResponse> responses = bookEntries.stream().map(ShelfEntryMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);

    }


    @PostMapping
    public ResponseEntity<Void> addBook(@RequestParam String googleBookId, Authentication authentication) {
        String username = authentication.getName();
        shelfService.addBookEntry(username, googleBookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PatchMapping("/entries/{entryId}/rating")
    public ResponseEntity<Void> updateRating(@PathVariable Integer entryId,
                                             @Valid @RequestBody UpdateRatingRequest request,
                                             Authentication authentication) {

        String username = authentication.getName();

        shelfService.updateBookRating(request.rating(), username, entryId);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/entries/{entryId}/status")
    public ResponseEntity<Void> updateReadingStatus(@Valid @RequestBody UpdateReadingRequest request,
                                                   @PathVariable Integer entryId,
                                                   Authentication authentication) {
        shelfService.updateReadingStatus(request.readingStatus(), entryId, authentication.getName());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer entryId, Authentication authentication) {
        shelfService.deleteBookEntry(entryId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}