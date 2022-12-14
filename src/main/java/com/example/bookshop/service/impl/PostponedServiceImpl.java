package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.PostponedService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostponedServiceImpl implements PostponedService {

    @Autowired
    private BookRatingService bookRatingService;
    @Autowired
    private BookService bookService;

    @Override
    public List<BookDto> getPostponedBooks(String postponedContents) {
        postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
        postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) :
                postponedContents;
        String[] cookieSlugs = postponedContents.split("/");
        List<Book> books = Arrays.stream(cookieSlugs)
                .map(slug -> bookService.getBook(slug))
                .collect(Collectors.toList());
        Map<String, BookRatingDto> bookRatings = new HashMap<>();
        bookRatingService.getBooksRating(books
                        .stream()
                        .map(Book::getSlug)
                        .collect(Collectors.toList()))
                .forEach(bookRatingDto -> bookRatings.put(bookRatingDto.getBookId(), bookRatingDto));
        return books
                .stream()
                .map(book -> {
                    BookDto bookDto = new BookDto(book);
                    BookRatingDto bookRating = bookRatings.get(book.getSlug());
                    if (bookRating != null) {
                        bookDto.setRating(bookRating.getRating());
                    }
                    return bookDto;
                }).collect(Collectors.toList());
    }
}
