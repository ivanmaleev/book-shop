package com.example.bookshop.service;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Интерфейс сервиса комментариев на книги
 */
@Service
public interface BookCommentService {

    /**
     * Сохраняет комментрий на книгу
     *
     * @param user               Пользователь
     * @param bookCommentRequest Комментрий на книгу
     * @return Результат сохранения
     */
    @Transactional
    BookComment saveBookComment(BookstoreUser user, BookCommentRequest bookCommentRequest);

    /**
     * Возвращает комментрии на книгу
     *
     * @param bookId Идентификатор книги
     * @return Список комментриев на книгу
     */
    List<BookCommentDto> getBookComments(String bookId);
}
