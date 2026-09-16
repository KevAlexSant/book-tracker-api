package org.kashboikev.booklist.shelf;

import java.util.List;

public record ShelfEntryResponse(
        Integer id,
        String googleBookId,
        String title,
        List<String> authors,
        Integer rating,
        ReadingStatus readingStatus
) {}
