package com.georgeradu.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import java.time.Clock;

@SpringBootApplication
@Validated
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
