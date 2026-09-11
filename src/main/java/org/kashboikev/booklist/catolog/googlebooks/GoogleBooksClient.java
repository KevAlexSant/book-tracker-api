package org.kashboikev.booklist.catolog.googlebooks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class GoogleBooksClient {

    private final RestClient googleBooksRestClient;

    public GoogleBooksClient(RestClient googleBooksRestClient) {
        this.googleBooksRestClient = googleBooksRestClient;
    }

    @Value("${MY_API_KEY}")
    private String apiKey;


    @Retryable(value = HttpServerErrorException.class, maxRetries = 3, delay = 1000)
    public GoogleBooksResponse searchBooks(String query) {

        log.info("Searching for query {}", query);


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

    @Retryable(value = HttpServerErrorException.class, maxRetries = 3, delay = 1000)
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
