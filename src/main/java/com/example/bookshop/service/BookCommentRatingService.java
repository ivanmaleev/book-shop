package com.example.bookshop.service;

import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.entity.BookCommentRating;
import com.example.bookshop.security.entity.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Интерфейс сервиса рейтинга комментариев на книги
 */
@Service
public interface BookCommentRatingService {

    /**
     * Сохраняет рейтинг комментария на кнингу и возвращает результат
     *
     * @param user                 Пользователь
     * @param commentRatingRequest Установленный рейтинг
     * @return Результат сохранения
     */
    @Transactional
    BookCommentRating saveBookCommentRating(BookstoreUser user, CommentRatingRequest commentRatingRequest);
}
