package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.repository.BookCommentRepository;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.service.BookCommentService;
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

@ActiveProfiles("test")
@SpringJUnitConfig(BookCommentServiceImplTest.TestConfig.class)
class BookCommentServiceImplTest {

    @Autowired
    private BookCommentService bookCommentService;
    @MockBean
    private BookCommentRepository bookCommentRepository;

    @Test
    void whenSaveBookComment() {
        final BookComment bookComment = new BookComment();
        bookComment.setId(1L);
        Mockito.when(bookCommentRepository.save(Mockito.any())).thenReturn(bookComment);
        Assertions.assertEquals(bookComment.getId(), bookCommentService.saveBookComment(new BookstoreUser(), new BookCommentRequest()).getId());
    }

    @Test
    void whenGetBookComments() {
        final BookCommentDto bookCommentDto = new BookCommentDto();
        bookCommentDto.setId(1L);
        Mockito.when(bookCommentRepository.findAllByBookId(Mockito.anyString())).thenReturn(List.of(bookCommentDto));
        Assertions.assertEquals(1, bookCommentService.getBookComments("bookId").size());
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public BookCommentService bookCommentService() {
            return new BookCommentServiceImpl();
        }
    }
}