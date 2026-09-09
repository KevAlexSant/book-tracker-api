package org.kashboikev.booklist.catolog.googlebooks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GoogleBooksClient {

    private final RestClient googleBooksRestClient;

    public GoogleBooksClient(RestClient googleBooksRestClient) {
        this.googleBooksRestClient = googleBooksRestClient;
    }

    @Value("${MY_API_KEY}")
    private String apiKey;

    public GoogleBooksResponse searchBooks(String query) {

        return googleBooksRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("maxResults", 40)
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
