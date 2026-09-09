package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.dto.GoogleBookItem;
import org.kashboikev.booklist.models.dto.GoogleBooksResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GoogleBooksService {

    private final RestClient googleBooksRestClient;

    public GoogleBooksService(RestClient googleBooksRestClient) {
        this.googleBooksRestClient = googleBooksRestClient;
    }

    @Value("${MY_API_KEY}")
    private String apiKey;

    public GoogleBooksResponse searchBooks(String query) {

        return googleBooksRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(GoogleBooksResponse.class);
    }

    public GoogleBookItem getBook(String id) {
        return googleBooksRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes/{id}")
                        .queryParam("key", apiKey)
                        .build(id))
                .retrieve()
                .body(GoogleBookItem.class);
    }


}
