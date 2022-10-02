package com.example.bookshop.entity;

import com.example.bookshop.security.BookstoreUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "book_comment", schema = "book_shop")
public class BookComment extends AbstractEntity {

    @Column(name = "text")
    public String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    private String bookId;

    @CreationTimestamp
    @Column(name = "date")
    private LocalDateTime date;

    public BookComment(String text, BookstoreUser user, String bookId) {
        this.text = text;
        this.user = user;
        this.bookId = bookId;
    }

    public BookComment(Long commentId) {
        this.setId(commentId);
    }
}
