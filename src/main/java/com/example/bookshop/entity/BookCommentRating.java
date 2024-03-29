package com.example.bookshop.entity;

import com.example.bookshop.security.entity.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Сущность комментария книги
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "book_comment_rating", schema = "book_shop")
public class BookCommentRating extends AbstractEntity {

    private static final long serialVersionUID = 1722955068543484831L;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private BookComment comment;
    @Column(name = "rating")
    private int rating = 0; // -1, 0, 1

    public BookCommentRating(BookstoreUser user, BookComment comment, Integer rating) {
        this.user = user;
        this.comment = comment;
        this.rating = getNormalizeRating(rating);
    }

    public void setRating(Integer rating) {
        this.rating = getNormalizeRating(rating);
    }

    private int getNormalizeRating(Integer rating) {
        return Objects.isNull(rating) ? 0 : Math.max(Math.min(rating, 1), -1);
    }
}
