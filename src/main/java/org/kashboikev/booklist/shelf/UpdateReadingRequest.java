package org.kashboikev.booklist.shelf;

import jakarta.validation.constraints.NotNull;

public record UpdateReadingRequest(
        @NotNull
        ReadingStatus readingStatus
){}
