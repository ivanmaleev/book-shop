package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.repository.BookRatingRepository;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.service.BookRatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringJUnitConfig(BookRatingServiceImplTest.TestConfig.class)
class BookRatingServiceImplTest {

    @Autowired
    private BookRatingService bookRatingService;

    @MockBean
    private BookRatingRepository bookRatingRepository;

    @Test
    void whenGetBookRating() {
        final BookRatingDto bookRatingDto = new BookRatingDto();
        bookRatingDto.setBookId("bookId");
        Mockito.when(bookRatingRepository.findBookRating(Mockito.anyString())).thenReturn(Optional.of(bookRatingDto));
        Assertions.assertEquals(bookRatingDto, bookRatingService.getBookRating("bookId"));
    }

    @Test
    void whenSaveBookRating() {
        final BookRating bookRating = new BookRating();
        bookRating.setId(1L);
        final BookRatingRequest bookRatingRequest = new BookRatingRequest();
        bookRatingRequest.setValue(1);
        bookRatingRequest.setBookId("bookId");
        final BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        Mockito.when(bookRatingRepository.findAllByUserAndBookId(bookstoreUser, bookRatingRequest.getBookId())).thenReturn(List.of(bookRating));
        Assertions.assertEquals(bookRatingRequest.getValue(), bookRatingService.saveBookRating(bookstoreUser, bookRatingRequest).getRating());
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public BookRatingService bookRatingService() {
            return new BookRatingServiceImpl();
        }
    }
}