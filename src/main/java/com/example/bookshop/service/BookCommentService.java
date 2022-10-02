package com.example.bookshop.service;

import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.entity.BookComment;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface BookCommentService {

    @Transactional
    BookComment saveBookComment(BookstoreUser currentUser, BookCommentRequest bookCommentRequest);

    List<BookComment> getBookComments(String bookId);
}
