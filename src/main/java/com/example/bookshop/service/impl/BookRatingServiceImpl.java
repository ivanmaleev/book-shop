package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.repository.BookRatingRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookRatingService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Реализация сервиса рейтинга книг
 */
public class BookRatingServiceImpl implements BookRatingService {

    @Autowired
    private BookRatingRepository bookRatingRepository;

    /**
     * Возвращает рейтинг книги
     *
     * @param bookId Id книги
     * @return Рейтинг книги
     */
    @Override
    public BookRatingDto getBookRating(String bookId) {
        return bookRatingRepository.findBookRating(bookId).orElseGet(() -> new BookRatingDto(bookId, 0, 0));
    }

    /**
     * Возвращает рейтинги книнг для списка id
     *
     * @param bookIds Список id кинг
     * @return Рейтинг книг
     */
    @Override
    public List<BookRatingDto> getBooksRating(List<String> bookIds) {
        return bookRatingRepository.findBooksRating(bookIds);
    }

    /**
     * Сохраняет рейтинг книги
     *
     * @param currentUser       Пользователь
     * @param bookRatingRequest Рейтинг книги для сохранения
     * @return Сохранённый рейтинг книги
     */
    @Transactional
    @Override
    public BookRating saveBookRating(BookstoreUser currentUser, BookRatingRequest bookRatingRequest) {
        List<BookRating> bookRating = bookRatingRepository.findAllByUserAndBookId(currentUser, bookRatingRequest.getBookId());
        if (!bookRating.isEmpty()) {
            bookRating.get(0).setRating(bookRatingRequest.getValue());
            return bookRating.get(0);
        } else {
            return bookRatingRepository.save(new BookRating(currentUser, bookRatingRequest.getBookId(), bookRatingRequest.getValue()));
        }
    }
}
