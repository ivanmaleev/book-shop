package com.example.bookshop.controllers;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookLocal;
import com.example.bookshop.security.BookstoreUserDetailsService;
import com.example.bookshop.security.jwt.JWTRequestFilter;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
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
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@WebMvcTest(AuthorsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookService<? extends Book> bookService;
    @MockBean
    private CommonService commonService;
    @MockBean
    private BookstoreUserDetailsService bookstoreUserDetailsService;
    @MockBean
    private JWTRequestFilter filter;

    @Test
    void authorsPage() throws Exception {
        Mockito.when(authorService.getAuthorsMap()).thenReturn(Map.of("name", List.of(new Author())));
        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("/authors/index"))
                .andExpect(model().attributeExists("authorsMap"))
                .andExpect(model().attributeExists("topbarActive"));

    }

    @Test
    void authorPage() throws Exception {
        Author author = new Author();
        author.setId(1L);
        Mockito.when(authorService.findById(author.getId())).thenReturn(author);
        Mockito.doReturn(List.of(new BookLocal())).when(bookService).getBooksByAuthor(author, 0, 20);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/slug/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/authors/slug"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("authorBooks"));
    }
}