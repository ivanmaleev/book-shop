package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.mapper.BookLocalMapper;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.PostponedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса отложенных книг
 */
@Slf4j
public class PostponedServiceImpl implements PostponedService {

    @Autowired
    private BookStatusService bookStatusService;
    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookLocalMapper bookMapper;

    /**
     * Возвращает списк отложенных кнги
     *
     * @param postponedContents Данные куки
     * @return Список книг
     */
    @Override
    public Collection<BookDto> getPostponedBooks(String postponedContents) {
        postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
        postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) :
                postponedContents;
        String[] cookieSlugs = postponedContents.split("/");
        Collection<BookDto> books = bookService.getBooks(Arrays.asList(cookieSlugs));

        Map<String, BookRatingDto> bookRatings = new HashMap<>();
        bookRatingService.getBooksRating(books
                        .stream()
                        .map(BookDto::getSlug)
                        .collect(Collectors.toList()))
                .forEach(bookRatingDto -> bookRatings.put(bookRatingDto.getBookId(), bookRatingDto));
        return books
                .stream()
                .peek(book -> {
                    BookRatingDto bookRating = bookRatings.get(book.getSlug());
                    if (Objects.nonNull(bookRating)) {
                        book.setRating(bookRating.getRating());
                    }
                }).collect(Collectors.toList());
    }

    /**
     * Добавляает книгу в отложенные
     *
     * @param bookCartRequest Книга для добавления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    @Override
    public void addBookToPostponed(BookCartRequest bookCartRequest, Cookie[] cookies, HttpServletResponse response, Model model) {
        bookStatusService.acceptRequestBookIdsToCookie("Postponed", bookCartRequest, cookies, response, model, Set::addAll);
    }

    /**
     * Удаляет книгу из отложенных
     *
     * @param bookCartRequest Книга для удаления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    @Override
    public void removeBookFromPostponed(BookCartRequest bookCartRequest, Cookie[] cookies, HttpServletResponse response, Model model) {
        bookStatusService.acceptRequestBookIdsToCookie("Postponed", bookCartRequest, cookies, response, model, Set::removeAll);
    }
}
