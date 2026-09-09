package org.kashboikev.booklist.catolog.googlebooks;

import java.util.List;

public record VolumeInfo(String title, List<String> authors, String description,
                         List<IndustryIdentifier> industryIdentifiers, ImageLinks imageLinks,
                         String publishedDate) {
}
