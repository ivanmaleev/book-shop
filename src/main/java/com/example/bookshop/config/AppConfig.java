package com.example.bookshop.config;

import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.GenreService;
import com.example.bookshop.service.LoadGenresService;
import com.example.bookshop.service.impl.BookRatingServiceImpl;
import com.example.bookshop.service.impl.BookServiceImpl;
import com.example.bookshop.service.impl.CartServiceImpl;
import com.example.bookshop.service.impl.CommonServiceImpl;
import com.example.bookshop.service.impl.GenreServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    @Bean
    public BookService bookService() {
        return new BookServiceImpl();
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
}
