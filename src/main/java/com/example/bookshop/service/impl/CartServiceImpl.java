package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.CartData;
import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.BookStatusService;
import com.example.bookshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления статусами книг
 */
public class CartServiceImpl implements CartService {

    static final private String CART_DELIMITER = "/";
    @Autowired
    private BookStatusService bookStatusService;
    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookService bookService;

    /**
     * Возвращает данные по суммам в карзине
     *
     * @param bookDtos Список книг
     * @return Суммовые данные для корзины
     */
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

    /**
     * Возвращает список книг из корзины
     *
     * @param cartContents Содерживое корзины куки
     * @return Список книг
     */
    @Override
    public List<BookDto> getCartBooks(String cartContents) {
        cartContents = cartContents.startsWith(CART_DELIMITER) ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith(CART_DELIMITER) ? cartContents.substring(0, cartContents.length() - 1) :
                cartContents;
        String[] cookieSlugs = cartContents.split(CART_DELIMITER);
        List<? extends Book> books = bookService.getBooks(Arrays.asList(cookieSlugs));

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
                    if (Objects.nonNull(bookRating)) {
                        bookDto.setRating(bookRating.getRating());
                    }
                    return bookDto;
                }).collect(Collectors.toList());
    }

    /**
     * Добавляет книгу в корзину
     *
     * @param bookCartRequest Книга для добавления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    @Override
    public void addBookToCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                              HttpServletResponse response, Model model) {
        bookStatusService.acceptRequestBookIdsToCookie("Cart", bookCartRequest, cookies, response, model, Set::addAll);
    }

    /**
     * Удаляет книгу из корзины
     *
     * @param bookCartRequest Книга для удаления
     * @param cookies         Куки
     * @param response        Исходящий http ответ
     * @param model           Модель страницы
     */
    @Override
    public void removeBookFromCart(BookCartRequest bookCartRequest, Cookie[] cookies,
                                   HttpServletResponse response, Model model) {
        bookStatusService.acceptRequestBookIdsToCookie("Cart", bookCartRequest, cookies, response, model, Set::removeAll);
    }
}
