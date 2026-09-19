package org.kashboikev.booklist.catalog.googlebooks;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.books")
public record GoogleBooksProperties(
        String apiKey,
        String baseUrl,
        int maxResults
){}
