package com.example.bookshop.repository;

import com.example.bookshop.entity.BookCommentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий рейтинга комментариев на книги
 */
@Repository
public interface BookCommentRatingRepository extends JpaRepository<BookCommentRating, Long> {

    /**
     * Возвращает рейтинг комментария на книгу
     *
     * @param userId    id пользователя
     * @param commentId id комментария
     * @return Рейтинг комментария на книгу
     */
    Optional<BookCommentRating> findTopByUserIdAndAndCommentId(Long userId, Long commentId);
}
