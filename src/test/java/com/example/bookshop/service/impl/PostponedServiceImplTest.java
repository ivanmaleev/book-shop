package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.PostponedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@ActiveProfiles("test")
@SpringJUnitConfig(PostponedServiceImplTest.TestConfig.class)
class PostponedServiceImplTest {

    @Autowired
    private PostponedService service;
    @MockBean
    private BookStatusService bookStatusService;
    @MockBean
    private BookRatingService bookRatingService;
    @MockBean
    private BookService<? extends Book> bookService;
    private HttpServletResponse httpServletResponse = new MockHttpServletResponse();
    private Model model = new ExtendedModelMap();

    @Test
    void getPostponedBooks() {
        BookLocal book = new BookLocal();
        book.setSlug("slug");
        Collection<? extends Book> books = List.of(book);
        Mockito.doReturn(books).when(bookService).getBooks(Mockito.anyList());
        Assertions.assertFalse(service.getPostponedBooks("/content/").isEmpty());
    }

    @Test
    void addBookToPostponed() {
        Assertions.assertDoesNotThrow(() -> service.addBookToPostponed(new BookCartRequest(), new Cookie[1], httpServletResponse, model));
    }

    @Test
    void removeBookFromPostponed() {
        Assertions.assertDoesNotThrow(() -> service.removeBookFromPostponed(new BookCartRequest(), new Cookie[1], httpServletResponse, model));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public PostponedService postponedService() {
            return new PostponedServiceImpl();
        }
    }
}