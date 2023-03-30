package com.example.bookshop.repository;

import com.example.bookshop.entity.BookCommentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookCommentRatingRepository extends JpaRepository<BookCommentRating, Long> {

    Optional<BookCommentRating> findTopByUserIdAndAndCommentId(Long userId, Long commentId);
}
