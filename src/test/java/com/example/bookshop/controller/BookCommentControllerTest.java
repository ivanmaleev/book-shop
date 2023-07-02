package com.example.bookshop.controller;

import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserDetailsService;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.security.jwt.JWTRequestFilter;
import com.example.bookshop.service.BookCommentRatingService;
import com.example.bookshop.service.BookCommentService;
import com.example.bookshop.service.CommonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BookCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookCommentService bookCommentService;
    @MockBean
    private BookstoreUserRegister userRegister;
    @MockBean
    private BookCommentRatingService bookCommentRatingService;
    @MockBean
    private CommonService commonService;
    @MockBean
    private BookstoreUserDetailsService bookstoreUserDetailsService;
    @MockBean
    private JWTRequestFilter filter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void rateBook() throws Exception {
        final BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        BookCommentRequest bookCommentRequest = new BookCommentRequest();
        bookCommentRequest.setBookId("id");
        bookCommentRequest.setText("text");
        Mockito.when(userRegister.getCurrentUser()).thenReturn(bookstoreUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/bookComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCommentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"result\":true}"));
    }

    @Test
    void testRateBook() throws Exception {
        final BookstoreUser bookstoreUser = new BookstoreUser();
        bookstoreUser.setId(1L);
        CommentRatingRequest commentRatingRequest = new CommentRatingRequest();
        commentRatingRequest.setCommentId(1L);
        commentRatingRequest.setValue(5);
        Mockito.when(userRegister.getCurrentUser()).thenReturn(bookstoreUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/rateBookComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRatingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"result\":true}"));
    }
}