package org.kashboikev.booklist.catalog.googlebooks;

import java.util.List;

public record GoogleBooksResponse(List<GoogleBookItem> items) {
}
