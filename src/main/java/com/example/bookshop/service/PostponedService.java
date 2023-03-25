package com.example.bookshop.service;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface PostponedService {

    List<BookDto> getPostponedBooks(String postponedContents);

    void addBookToPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                            HttpServletResponse response, Model model);
    void removeBookFromPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                                 HttpServletResponse response, Model model);
}
