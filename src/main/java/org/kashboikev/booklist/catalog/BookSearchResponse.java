package org.kashboikev.booklist.catalog;

import java.util.List;

public record BookSearchResponse(
        String googleBookId,
        String title,
        List<String> authors,
        String publishedDate,
        String isbn10,
        String isbn13,
        String imageUrl

){}
