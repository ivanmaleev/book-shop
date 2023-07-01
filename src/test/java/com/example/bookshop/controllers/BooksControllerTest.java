package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserDetailsService;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.security.jwt.JWTRequestFilter;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookCommentService;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.UsersBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.example.bookshop.constants.BookStatus.CART;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@WebMvcTest(BooksController.class)
@AutoConfigureMockMvc(addFilters = false)
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService<? extends Book> bookService;
    @MockBean
    private UsersBookService usersBookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookRatingService bookRatingService;
    @MockBean
    private BookCommentService bookCommentService;
    @MockBean
    private BookstoreUserRegister userRegister;
    @MockBean
    private CommonService commonService;
    @MockBean
    private BookstoreUserDetailsService bookstoreUserDetailsService;
    @MockBean
    private JWTRequestFilter filter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void bookPage() throws Exception {
        final BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        bookstoreUser.setName("user");
        BookRatingDto bookRating = new BookRatingDto();
        bookRating.setBookId("1");
        bookRating.setRating(5);
        bookRating.setRatingCounter(1);
        BookCommentDto bookCommentDto = new BookCommentDto();
        bookCommentDto.setId(1L);
        bookCommentDto.setUser(bookstoreUser);
        Book book = new BookLocal();
        book.setTitle("title");
        Mockito.when(userRegister.getCurrentUser()).thenReturn(bookstoreUser);
        Mockito.doReturn(book).when(bookService).getBook("1");
        Mockito.when(usersBookService.getBookStatus(1L, "1")).thenReturn(CART);
        Mockito.when(bookRatingService.getBookRating("1")).thenReturn(bookRating);
        Mockito.when(bookCommentService.getBookComments("1")).thenReturn(List.of(bookCommentDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/books/slug/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/slugmy"))
                .andExpect(model().attributeExists("slugBook"))
                .andExpect(model().attributeExists("bookRating"))
                .andExpect(model().attributeExists("bookComments"));
    }

    @Test
    void getRecommendedBooksPage() {
    }

    @Test
    void getRecentBooksPage() {
    }

    @Test
    void getPopularBooksPage() {
    }

    @Test
    void getGenreBooksPage() {
    }

    @Test
    void getAuthorsBooksPage() {
    }

    @Test
    void rateBook() {
    }
}