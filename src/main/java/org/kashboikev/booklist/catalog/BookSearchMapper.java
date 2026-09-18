package org.kashboikev.booklist.catalog;

import org.kashboikev.booklist.catalog.googlebooks.GoogleBookItem;

public class BookSearchMapper {

    public static BookSearchResponse toResponse(GoogleBookItem googleBookItem) {
        var volumeInfo = googleBookItem.volumeInfo();

        String isbn10 = null;
        String isbn13 = null;

        if (volumeInfo.industryIdentifiers() != null) {
            for (var id : volumeInfo.industryIdentifiers()) {
                if ("ISBN_10".equals(id.type())) {
                    isbn10 = id.identifier();
                } else if ("ISBN_13".equals(id.type())) {
                    isbn13 = id.identifier();
                }
            }
        }

        String thumbnailUrl = null;
        if (volumeInfo.imageLinks() != null) {
            thumbnailUrl = volumeInfo.imageLinks().thumbnail();
        }

        return new BookSearchResponse(
                googleBookItem.id(),
                volumeInfo.title(),
                volumeInfo.authors(),
                volumeInfo.publishedDate(),
                isbn10,
                isbn13,
                thumbnailUrl
        );
    }
}
