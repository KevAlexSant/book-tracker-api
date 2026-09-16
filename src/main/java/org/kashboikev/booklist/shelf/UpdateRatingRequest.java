package org.kashboikev.booklist.shelf;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateRatingRequest(
        @Min(value = 1)
        @Max(value = 5)
        Integer rating

) {}
