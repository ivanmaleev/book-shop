package com.example.bookshop.service;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.BookRatingRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface BookRatingService {

    BookRatingDto getBookRating(String bookId);

    @Transactional
    BookRating saveBookRating(BookstoreUser currentUser, BookRatingRequest bookRatingRequest);
}
