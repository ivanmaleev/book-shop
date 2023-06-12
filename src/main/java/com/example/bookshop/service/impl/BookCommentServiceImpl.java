package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.repository.BookCommentRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookCommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Реализация сервиса комментариев на книги
 */
public class BookCommentServiceImpl implements BookCommentService {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    /**
     * Сохраняет комментарий на книгу
     *
     * @param user               Пользователь
     * @param bookCommentRequest Комментрий на книгу
     * @return Результат сохранения
     */
    @Override
    public BookComment saveBookComment(BookstoreUser user, BookCommentRequest bookCommentRequest) {
        return bookCommentRepository.save(new BookComment(bookCommentRequest.getText(), user, bookCommentRequest.getBookId()));
    }

    /**
     * Возвращает комментрии на книгу
     *
     * @param bookId Идентификатор книги
     * @return Список комментриев на книгу
     */
    @Override
    public Collection<BookCommentDto> getBookComments(String bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }
}
