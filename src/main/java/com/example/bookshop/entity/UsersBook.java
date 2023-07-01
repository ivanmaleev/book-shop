package com.example.bookshop.entity;

import com.example.bookshop.security.entity.BookstoreUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Сущность пользовательской книги
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UsersBook usersBook = (UsersBook) o;
        return archived == usersBook.archived && Objects.equals(user, usersBook.user) && Objects.equals(bookId, usersBook.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, bookId, archived);
    }
}
