package com.example.bookshop.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class BooksPageDto {

    private Integer count;
    private Collection<BookDto> books;

    public BooksPageDto(Collection<BookDto> books) {
        this.count = books.size();
        this.books = books;
    }
}
