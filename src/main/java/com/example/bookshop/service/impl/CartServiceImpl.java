package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.service.CartService;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class CartServiceImpl implements CartService {

    @Override
    public void changeBookStatus(BookCartRequest bookCartRequest, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        if (bookCartRequest != null && bookCartRequest.getStatus() != null) {
            switch (bookCartRequest.getStatus()) {
                case KEPT:
                    addBookToPostponed(bookCartRequest, request.getCookies(), response, model);
                    break;
                case CART:
                    addBookToCart(bookCartRequest, request.getCookies(), response, model);
                    break;
                case ARCHIVED:
                    break;
                case UNLINK:
                    removeBookFromCart(bookCartRequest, request.getCookies(), response, model);
                    break;
                case UNLINK_POSTPONED:
                    removeBookFromPostponed(bookCartRequest, request.getCookies(), response, model);
                    break;
            }
        }
    }

    private void addBookToCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                               HttpServletResponse response, Model model) {
        addBookToCookie("Cart", bookCartRequest, cookies, response, model);
    }

    private void addBookToPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                                    HttpServletResponse response, Model model) {
        addBookToCookie("Postponed", bookCartRequest, cookies, response, model);
    }

    private void removeBookFromCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                                    HttpServletResponse response, Model model) {
        removeBookFromCookie("Cart", bookCartRequest, cookies, response, model);
    }

    private void removeBookFromPostponed(BookCartRequest bookCartRequest, Cookie[] cookies,
                                         HttpServletResponse response, Model model) {
        removeBookFromCookie("Postponed", bookCartRequest, cookies, response, model);
    }

    private void addBookToCookie(String cookieName, BookCartRequest bookCartRequest, Cookie[] cookies,
                                 HttpServletResponse response, Model model) {
        String cookieNameContent = cookieName.toLowerCase() + "Contents";
        String cookieNameEmpty = "is" + cookieName + "Empty";
        Optional<Cookie> cartContents = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieNameContent))
                .findFirst();
        if (!cartContents.isPresent() || cartContents.get().getValue().equals("")) {
            Cookie cookie = new Cookie(cookieNameContent, bookCartRequest.getBookId());
            cookie.setPath("/");
            response.addCookie(cookie);
        } else if (!cartContents.get().getValue().contains(bookCartRequest.getBookId())) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents.get().getValue()).add(bookCartRequest.getBookId());
            Cookie cookie = new Cookie(cookieNameContent, stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        model.addAttribute(cookieNameEmpty, false);
    }

    private void removeBookFromCookie(String cookieName, BookCartRequest bookCartRequest, Cookie[] cookies,
                                      HttpServletResponse response, Model model) {
        String cookieNameContent = cookieName.toLowerCase() + "Contents";
        String cookieNameEmpty = "is" + cookieName + "Empty";
        Optional<Cookie> cartContents = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieNameContent))
                .findFirst();
        if (cartContents.isPresent() && !cartContents.get().getValue().equals("")) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.get().getValue().split("/")));
            cookieBooks.remove(bookCartRequest.getBookId());
            Cookie cookie = new Cookie(cookieNameContent, String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(cookieNameEmpty, false);
        } else {
            model.addAttribute(cookieNameEmpty, true);
        }
    }
}
