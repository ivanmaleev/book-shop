package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookCartRequest;
import com.example.bookshop.service.CartService;
import lombok.SneakyThrows;
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
    public void changeBookStatus(BookCartRequest bookCartRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
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
            }
        }
    }

    private void addBookToPostponed(BookCartRequest bookCartRequest, Cookie[] cookies, HttpServletResponse response, Model model) {
        Optional<Cookie> postponedContents = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("postponedContents"))
                .findFirst();
        if (!postponedContents.isPresent() || postponedContents.get().getValue().equals("")) {
            Cookie cookie = new Cookie("postponedContents", bookCartRequest.getBookId());
            cookie.setPath("/");
            response.addCookie(cookie);
        } else if (!postponedContents.get().getValue().contains(bookCartRequest.getBookId())) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedContents.get().getValue()).add(bookCartRequest.getBookId());
            Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.get().getValue().split("/")));
            cookieBooks.remove(bookCartRequest.getBookId());
            Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        }
    }


    @SneakyThrows
    private void addBookToCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                               HttpServletResponse response, Model model) {
        Optional<Cookie> cartContents = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("cartContents"))
                .findFirst();
        if (!cartContents.isPresent() || cartContents.get().getValue().equals("")) {
            Cookie cookie = new Cookie("cartContents", bookCartRequest.getBookId());
            cookie.setPath("/");
            response.addCookie(cookie);
        } else if (!cartContents.get().getValue().contains(bookCartRequest.getBookId())) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents.get().getValue()).add(bookCartRequest.getBookId());
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        model.addAttribute("isCartEmpty", false);
    }

    private void removeBookFromCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                                    HttpServletResponse response, Model model) {
        Optional<Cookie> cartContents = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("cartContents"))
                .findFirst();
        if (cartContents.isPresent() && !cartContents.get().getValue().equals("")) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.get().getValue().split("/")));
            cookieBooks.remove(bookCartRequest.getBookId());
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
    }
}
