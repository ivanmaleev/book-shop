package com.example.bookshop.service;

import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.entity.BookCommentRating;
import com.example.bookshop.security.BookstoreUser;
import org.springframework.stereotype.Service;

@Service
public interface BookCommentRatingService {

    BookCommentRating saveBookCommentRating(BookstoreUser user, CommentRatingRequest commentRatingRequest);
}
