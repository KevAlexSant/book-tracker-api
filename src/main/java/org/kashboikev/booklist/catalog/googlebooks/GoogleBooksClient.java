package org.kashboikev.booklist.catalog.googlebooks;

import lombok.extern.slf4j.Slf4j;
import org.kashboikev.booklist.securityconfig.GoogleBooksProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class GoogleBooksClient {

    private final RestClient googleBooksRestClient;
    private final GoogleBooksProperties googleBooksProperties;

    public GoogleBooksClient(
            RestClient googleBooksRestClient,
            GoogleBooksProperties googleBooksProperties) {

        this.googleBooksRestClient = googleBooksRestClient;
        this.googleBooksProperties = googleBooksProperties;
    }

    @Retryable(
            value = HttpServerErrorException.class,
            maxRetries = 3,
            delay = 1000
    )
    public GoogleBooksResponse searchBooks(String query) {

        log.info("Searching for query {}", query);

        return googleBooksRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam(
                                "maxResults",
                                googleBooksProperties.maxResults()
                        )
                        .queryParam(
                                "key",
                                googleBooksProperties.apiKey()
                        )
                        .build())
                .retrieve()
                .body(GoogleBooksResponse.class);
    }

    @Retryable(
            value = HttpServerErrorException.class,
            maxRetries = 3,
            delay = 1000
    )
    public GoogleBookItem getBook(String id) {

        return googleBooksRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes/{id}")
                        .queryParam(
                                "key",
                                googleBooksProperties.apiKey()
                        )
                        .build(id))
                .retrieve()
                .body(GoogleBookItem.class);
    }
}
