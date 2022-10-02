package com.example.bookshop.service;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface BookRatingService {

    BookRatingDto getBookRating(String bookId);

    List<BookRatingDto> getBooksRating(List<String> bookIds);

    @Transactional
    BookRating saveBookRating(BookstoreUser currentUser, BookRatingRequest bookRatingRequest);
}
