package org.kashboikev.booklist.catalog.googlebooks;

import lombok.extern.slf4j.Slf4j;
import org.kashboikev.booklist.securityconfig.GoogleBooksProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class GoogleBooksClientConfig {

    private final GoogleBooksProperties googleBooksProperties;
    private final org.springframework.core.env.Environment environment;

    @Autowired
    public GoogleBooksClientConfig(GoogleBooksProperties googleBooksProperties, org.springframework.core.env.Environment environment) {
        this.googleBooksProperties = googleBooksProperties;
        this.environment = environment;
    }

    @Bean
    public RestClient googleBooksRestClient() {

        return RestClient.builder()
                .baseUrl(googleBooksProperties.baseUrl())
                .build();
    }

}
