package org.kashboikev.booklist.shelf;

import org.junit.jupiter.api.Test;
import org.kashboikev.booklist.catalog.googlebooks.GoogleBooksClient;
import org.kashboikev.booklist.catalog.googlebooks.GoogleBooksResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


class GoogleBooksClientTest {

    @Test
    void searchBooks_sendsExpectedRequest_andDeserializesResponse() {



        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://www.googleapis.com/books/v1");

        MockRestServiceServer server =
                MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.build();

        GoogleBooksClient googleBooksClient =
                new GoogleBooksClient(restClient, "test-key");


        String responseJson = """
                {
                  "items": [
                    {
                      "id": "123",
                      "volumeInfo": {
                        "title": "Harry Potter"
                      }
                    }
                  ]
                }
                """;


        server.expect(requestTo(startsWith(
                        "https://www.googleapis.com/books/v1/volumes")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("q", "Harry%20Potter"))
                .andExpect(queryParam("maxResults", "40"))
                .andExpect(queryParam("key", "test-key"))
                .andRespond(
                        withSuccess(
                                responseJson,
                                MediaType.APPLICATION_JSON
                        )
                );




        GoogleBooksResponse result =
                googleBooksClient.searchBooks("Harry Potter");



        assertEquals(1, result.items().size());

        assertEquals(
                "123",
                result.items().get(0).id()
        );

        assertEquals(
                "Harry Potter",
                result.items().get(0).volumeInfo().title()
        );

        server.verify();
    }
}
