package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CartServiceImpl implements CartService {

    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookService bookService;

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

    public CartData getCartData(List<BookDto> bookDtos) {
        return new CartData(bookDtos
                .stream()
                .map(BookDto::getPrice)
                .reduce(Integer::sum)
                .orElse(0),
                bookDtos
                        .stream()
                        .map(BookDto::getPriceOld)
                        .reduce(Integer::sum)
                        .orElse(0));
    }

    @Override
    public List<BookDto> getCartBooks(String cartContents) {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) :
                cartContents;
        String[] cookieSlugs = cartContents.split("/");
        List<Book> books = Arrays.stream(cookieSlugs)
                .map(slug -> bookService.getBook(slug))
                .collect(Collectors.toList());
        Map<String, BookRatingDto> bookRatings = new HashMap<>();
        bookRatingService.getBooksRating(books
                        .stream()
                        .map(Book::getSlug)
                        .collect(Collectors.toList()))
                .forEach(bookRatingDto -> bookRatings.put(bookRatingDto.getBookId(), bookRatingDto));
        return books
                .stream()
                .map(book -> {
                    BookDto bookDto = new BookDto(book);
                    BookRatingDto bookRating = bookRatings.get(book.getSlug());
                    if (bookRating != null) {
                        bookDto.setRating(bookRating.getRating());
                    }
                    return bookDto;
                }).collect(Collectors.toList());
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
        if (!cartContents.isPresent() || StringUtils.isBlank(cartContents.get().getValue())) {
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
