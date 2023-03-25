package com.example.bookshop.service;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.dto.request.BookCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@Service
public interface CartService {

    List<BookDto> getCartBooks(String cartContents);

    CartData getCartData(List<BookDto> bookDtos);

    void addBookToCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                               HttpServletResponse response, Model model);

    void removeBookFromCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                                    HttpServletResponse response, Model model);
}
