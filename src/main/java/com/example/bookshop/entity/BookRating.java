package com.example.bookshop.entity;

import com.example.bookshop.security.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "book_rating", schema = "book_shop")
public class BookRating extends AbstractEntity {

    private static final long serialVersionUID = -9222633831955107069L;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;
    @Column(name = "book_id")
    private String bookId;
    @Column(name = "rating")
    private int rating = 1;

    public BookRating(BookstoreUser user, String bookId, Integer rating) {
        this.user = user;
        this.bookId = bookId;
        this.rating = getNormalizeRating(rating);
    }

    public void setRating(Integer rating) {
        this.rating = getNormalizeRating(rating);
    }

    private int getNormalizeRating(Integer rating) {
        return Objects.isNull(rating) ? 1 : Math.max(Math.min(rating, 5), 1);
    }
}
