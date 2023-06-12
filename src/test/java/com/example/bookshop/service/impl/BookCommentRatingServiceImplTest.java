package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.entity.BookCommentRating;
import com.example.bookshop.repository.BookCommentRatingRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookCommentRatingService;
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

import java.util.Optional;

@ActiveProfiles("test")
@SpringJUnitConfig(BookCommentRatingServiceImplTest.TestConfig.class)
class BookCommentRatingServiceImplTest {

    @Autowired
    private BookCommentRatingService bookCommentRatingService;

    @MockBean
    private BookCommentRatingRepository bookCommentRatingRepository;

    @Test
    void whenSaveBookCommentRating() {
        final BookCommentRating bookCommentRating = new BookCommentRating();
        bookCommentRating.setId(1L);
        final CommentRatingRequest commentRatingRequest = new CommentRatingRequest();
        commentRatingRequest.setValue(2);
        Mockito.when(bookCommentRatingRepository.findTopByUserIdAndAndCommentId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(bookCommentRating));
        Mockito.when(bookCommentRatingRepository.save(Mockito.any()))
                .thenReturn(bookCommentRating);
        Assertions.assertEquals(bookCommentRating,
                bookCommentRatingService.saveBookCommentRating(new BookstoreUser(), commentRatingRequest));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public BookCommentRatingService bookCommentRatingService() {
            return new BookCommentRatingServiceImpl();
        }
    }
}