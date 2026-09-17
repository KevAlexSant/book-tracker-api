package org.kashboikev.booklist.shelf;

public class ShelfEntryMapper {

    public static ShelfEntryResponse toResponse(BookEntry bookEntry) {
        return new ShelfEntryResponse(
                bookEntry.getId(),
                bookEntry.getBook().getGoogleBookId(),
                bookEntry.getBook().getTitle(),
                bookEntry.getBook().getAuthors(),
                bookEntry.getBook().getDescription(),
                bookEntry.getBook().getPublishedDate(),
                bookEntry.getBook().getIsbn10(),
                bookEntry.getBook().getIsbn13(),
                bookEntry.getRating(),
                bookEntry.getReadingStatus()
        );
    }
}
