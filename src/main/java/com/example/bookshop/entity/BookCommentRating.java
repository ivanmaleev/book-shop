package com.example.bookshop.entity;

import com.example.bookshop.security.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "book_comment_rating", schema = "book_shop")
public class BookCommentRating extends AbstractEntity {

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
        return rating == null ? 0 : Math.max(Math.min(rating, 1), -1);
    }
}
