package com.example.bookshop.entity;

import com.example.bookshop.security.BookstoreUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users_book", schema = "book_shop")
public class UsersBook extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "archived")
    private boolean archived;

    public UsersBook(BookstoreUser user, String bookId, boolean archived) {
        this.user = user;
        this.bookId = bookId;
        this.archived = archived;
    }
}
