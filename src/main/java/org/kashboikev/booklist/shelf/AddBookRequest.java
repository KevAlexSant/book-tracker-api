package org.kashboikev.booklist.shelf;

import jakarta.validation.constraints.NotBlank;

public record AddBookRequest(
        @NotBlank(message = "Google Book ID is required")
        String googleBookId

){}
