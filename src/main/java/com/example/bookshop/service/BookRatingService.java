package com.example.bookshop.service;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Интерфейс сервиса рейтинга книг
 */
@Service
public interface BookRatingService {

    /**
     * Возвращает рейтинг книги
     *
     * @param bookId Id книги
     * @return Рейтинг книги
     */
    BookRatingDto getBookRating(String bookId);

    /**
     * Возвращает рейтинги книнг для списка id
     *
     * @param bookIds Список id кинг
     * @return Рейтинг книг
     */
    List<BookRatingDto> getBooksRating(List<String> bookIds);

    /**
     * Сохраняет рейтинг книги
     *
     * @param currentUser       Пользователь
     * @param bookRatingRequest Рейтинг книги для сохранения
     * @return Сохранённый рейтинг книги
     */
    @Transactional
    BookRating saveBookRating(BookstoreUser currentUser, BookRatingRequest bookRatingRequest);
}
