package com.example.bookshop.config;

import com.example.bookshop.service.BookCommentRatingService;
import com.example.bookshop.service.BookCommentService;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.impl.BookStatusServiceImpl;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.GenreService;
import com.example.bookshop.service.PostponedService;
import com.example.bookshop.service.impl.BookCommentRatingServiceImpl;
import com.example.bookshop.service.impl.BookCommentServiceImpl;
import com.example.bookshop.service.impl.BookRatingServiceImpl;
import com.example.bookshop.service.impl.BookServiceGoogleApiImpl;
import com.example.bookshop.service.impl.BookServiceLocalImpl;
import com.example.bookshop.service.impl.CartServiceImpl;
import com.example.bookshop.service.impl.CommonServiceImpl;
import com.example.bookshop.service.impl.GenreServiceImpl;
import com.example.bookshop.service.impl.PostponedServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Общая конфигурация
 */
@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    @Bean
    @ConditionalOnProperty(value = "google.books.api.enable", havingValue = "true")
    public BookService bookServiceGoogleApi() {
        return new BookServiceGoogleApiImpl();
    }
    @Bean
    @ConditionalOnProperty(value = "google.books.api.enable", havingValue = "false")
    public BookService bookServiceLocal() {
        return new BookServiceLocalImpl();
    }
    @Bean
    public GenreService genreService(){
        return new GenreServiceImpl();
    }
    @Bean
    public CartService cartService(){
        return new CartServiceImpl();
    }
    @Bean
    public CommonService commonService(){
        return new CommonServiceImpl();
    }
    @Bean
    public BookRatingService bookRatingService(){
        return new BookRatingServiceImpl();
    }
    @Bean
    public BookCommentService bookCommentService(){
        return new BookCommentServiceImpl();
    }
    @Bean
    public BookCommentRatingService bookCommentRatingService(){
        return new BookCommentRatingServiceImpl();
    }
    @Bean
    public PostponedService postponedService(){
        return new PostponedServiceImpl();
    }
    @Bean
    public BookStatusService bookStatusService(){
        return new BookStatusServiceImpl();
    }
}
