package com.example.bookshop.data;

import com.example.bookshop.entity.Book;

import java.util.Collection;
import java.util.List;

public class BooksPageDto {

    private Integer count;
    private Collection<? extends Book> books;

    public BooksPageDto(Collection<? extends Book> books) {
        this.count = books.size();
        this.books = books;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Collection<? extends Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
