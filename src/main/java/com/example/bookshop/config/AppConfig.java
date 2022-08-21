package com.example.bookshop.config;

import com.example.bookshop.service.BookService;
import com.example.bookshop.service.impl.BookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BookService bookService() {
        return new BookServiceImpl();
    }
}
