package org.kashboikev.booklist.models.dto;

import java.util.List;

public record GoogleBooksResponse(List<GoogleBookItem> items) {
}
