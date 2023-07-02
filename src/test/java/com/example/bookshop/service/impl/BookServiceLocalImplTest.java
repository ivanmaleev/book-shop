package com.example.bookshop.service.impl;

import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.mapper.BookLocalMapper;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.UsersBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ActiveProfiles("test")
@SpringJUnitConfig(BookServiceLocalImplTest.TestConfig.class)
class BookServiceLocalImplTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookLocalMapper bookLocalMapper;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private UsersBookService usersBookService;

    @Test
    void getBooksByAuthor() {
        Mockito.when(bookRepository.findAllByAuthorId(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(List.of(new BookLocal())));
        Assertions.assertEquals(1, bookService.getBooksByAuthor(new AuthorDto(), 1, 1).size());
    }

    @Test
    void getPageOfRecommendedBooks() {
        Mockito.when(bookRepository.findAllByIsBestseller(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(List.of(new BookLocal())));
        Assertions.assertEquals(1, bookService.getPageOfRecommendedBooks(1, 1).size());
    }

    @Test
    void getPageOfRecentBooks() {
        Mockito.when(bookRepository.findAllByPubDateBetween(Mockito.any(), Mockito.any(), Mockito.any())).
                thenReturn(new PageImpl<>(List.of(new BookLocal())));
        Assertions.assertEquals(1, bookService.getPageOfRecentBooks(1, 1, LocalDate.now(), LocalDate.now()).size());
    }

    @Test
    void getPageOfPopularBooks() {
        Mockito.when(bookRepository.findAllByIsBestseller(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(List.of(new BookLocal())));
        Assertions.assertEquals(1, bookService.getPageOfPopularBooks(1, 1).size());
    }

    @Test
    void getBook() throws Exception {
        BookLocal bookLocal = new BookLocal();
        bookLocal.setId("1");
        Mockito.when(bookRepository.findTopBySlug(Mockito.anyString())).thenReturn(Optional.of(bookLocal));
        Assertions.assertEquals(bookLocal.getId(), bookService.getBook("book").getId());
    }

    @Test
    void getBookThrows() {
        final BookLocal bookLocal = new BookLocal();
        bookLocal.setId("1");
        Mockito.when(bookRepository.findTopBySlug(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () -> bookService.getBook("book"));
    }

    @Test
    void getBooks() {
        Mockito.when(bookRepository.findAllBySlugIn(Mockito.any())).thenReturn(List.of(new BookLocal()));
        Assertions.assertEquals(1, bookService.getBooks(Collections.emptyList()).size());
    }

    @Test
    void getBooksByGenreId() {
        Mockito.when(bookRepository.findAllByGenre(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(List.of(new BookLocal())));
        Assertions.assertEquals(1, bookService.getBooksByGenreId(1L, 1, 1).size());
    }

    @Test
    void findUsersBooks() {
        Mockito.when(bookRepository.findAllBySlugIn(Mockito.any())).thenReturn(List.of(new BookLocal()));
        Assertions.assertEquals(1, bookService.findUsersBooks(1L, false).size());
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public BookService bookService() {
            return new BookServiceLocalImpl();
        }
        @Bean
        public BookLocalMapper bookLocalMapper() {
            return Mappers.getMapper(BookLocalMapper.class);
        }
    }
}