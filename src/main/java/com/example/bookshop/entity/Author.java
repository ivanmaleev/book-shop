package com.example.bookshop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Сущность авторов книг
 */
@Entity
@Table(name = "author", schema = "book_shop")
@Data
public class Author extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -8510159208698888882L;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public Author(List<String> authors) {
        if (Objects.nonNull(authors)) {
            this.lastName = authors.toString();
        }
        this.firstName = "";
    }

    public Author() {
        this.firstName = "";
        this.lastName = "";
    }

    @Override
    public String toString() {
        return String.format("%s %s", lastName, firstName);
    }
}
