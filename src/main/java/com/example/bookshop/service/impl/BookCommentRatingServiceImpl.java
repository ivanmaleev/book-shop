package com.example.bookshop.service.impl;

import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.entity.BookCommentRating;
import com.example.bookshop.repository.BookCommentRatingRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookCommentRatingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Реализация сервиса рейтинга комментариев на книги
 */
public class BookCommentRatingServiceImpl implements BookCommentRatingService {

    @Autowired
    private BookCommentRatingRepository bookCommentRatingRepository;

    /**
     * Сохраняет рейтинг комментария на кнингу и возвращает результат
     *
     * @param user                 Пользователь
     * @param commentRatingRequest Установленный рейтинг
     * @return Результат сохранения
     */
    @Override
    public BookCommentRating saveBookCommentRating(BookstoreUser user, CommentRatingRequest commentRatingRequest) {
        return bookCommentRatingRepository.
                findTopByUserIdAndAndCommentId(user.getId(), commentRatingRequest.getCommentId())
                .map(bookCommentRating -> {
                    bookCommentRating.setRating(commentRatingRequest.getValue());
                    return bookCommentRatingRepository.save(bookCommentRating);
                }).orElseGet(() -> bookCommentRatingRepository.save(new BookCommentRating(user,
                        new BookComment(commentRatingRequest.getCommentId()), commentRatingRequest.getValue())));
    }
}
