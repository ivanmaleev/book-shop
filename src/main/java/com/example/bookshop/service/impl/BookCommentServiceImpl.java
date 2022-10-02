package com.example.bookshop.service.impl;

import com.example.bookshop.dto.BookCommentDto;
import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.repository.BookCommentRepository;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.service.BookCommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookCommentServiceImpl implements BookCommentService {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Override
    public BookComment saveBookComment(BookstoreUser currentUser, BookCommentRequest bookCommentRequest) {
        return bookCommentRepository.save(new BookComment(bookCommentRequest.getText(), currentUser, bookCommentRequest.getBookId()));
    }

    @Override
    public List<BookCommentDto> getBookComments(String bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }
}
