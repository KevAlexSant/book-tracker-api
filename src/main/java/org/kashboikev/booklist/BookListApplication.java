package org.kashboikev.booklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class BookListApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookListApplication.class, args);
    }

}
