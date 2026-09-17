package org.kashboikev.booklist.shelf;

import java.util.List;

public record ShelfEntryResponse(
        Integer id,
        String googleBookId,
        String title,
        List<String> authors,
        String description,
        String publishedDate,
        String isbn10,
        String isbn13,
        Integer rating,
        ReadingStatus readingStatus
) {}
