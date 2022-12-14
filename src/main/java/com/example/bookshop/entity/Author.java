package com.example.bookshop.entity;

import com.example.bookshop.entity.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "author", schema = "book_shop")
@Data
public class Author extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -8510159208698888882L;
    private String firstName;
    private String lastName;


//    @OneToMany(mappedBy = "author")
//    @JsonIgnore
//    private List<Book> bookList = new ArrayList<>();

    public Author(List<String> authors) {
        if (authors != null) {
            this.lastName = authors.toString();
        }
        this.firstName = "";
    }

    public Author() {
        this.firstName = "";
        this.lastName = "";
    }

//    public List<Book> getBookList() {
//        return bookList;
//    }
//
//    public void setBookList(List<Book> bookList) {
//        this.bookList = bookList;
//    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }
}
