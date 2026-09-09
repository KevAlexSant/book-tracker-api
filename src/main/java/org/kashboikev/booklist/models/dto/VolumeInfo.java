package org.kashboikev.booklist.models.dto;

import java.util.List;

public record VolumeInfo(String title, List<String> authors, String description,
                         List<IndustryIdentifier> industryIdentifiers, ImageLinks imageLinks,
                         String publishedDate) {
}
