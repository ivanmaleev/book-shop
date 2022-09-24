package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.BookRatingRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.BookRating;
import com.example.bookshop.repository.BookRatingRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookRatingService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class BookRatingServiceImpl implements BookRatingService {

    @Autowired
    private BookRatingRepository bookRatingRepository;

    @Override
    public BookRatingDto getBookRating(String bookId) {
        Optional<BookRatingDto> bookRating = bookRatingRepository.findBookRating(bookId);
        return bookRating.orElseGet(() -> new BookRatingDto(bookId, 0, 0));
    }


    @Transactional
    @Override
    public BookRating saveBookRating(BookstoreUser currentUser, BookRatingRequest bookRatingRequest) {
        List<BookRating> bookRating = bookRatingRepository.findAllByUserAndAndBookId(currentUser, bookRatingRequest.getBookId());
        if (!bookRating.isEmpty()) {
            bookRating.get(0).setRating(bookRatingRequest.getValue());
            return bookRating.get(0);
        } else {
            return bookRatingRepository.save(new BookRating(currentUser, bookRatingRequest.getBookId(), bookRatingRequest.getValue()));
        }
    }
}
