package com.example.bookshop.controllers;

import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.dto.response.BookCommentResponse;
import com.example.bookshop.dto.response.CommentRatingResponse;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookCommentRatingService;
import com.example.bookshop.service.BookCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookCommentController {
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private BookCommentRatingService bookCommentRatingService;

    @PostMapping("/bookComment")
    @ResponseBody
    public BookCommentResponse rateBook(@RequestBody BookCommentRequest bookCommentRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        boolean result = false;
        if (!currentUser.isAnonymousUser()) {
            bookCommentService.saveBookComment(currentUser, bookCommentRequest);
            result = true;
        }
        return new BookCommentResponse(result);
    }

    @PostMapping("/rateBookComment")
    @ResponseBody
    public CommentRatingResponse rateBook(@RequestBody CommentRatingRequest commentRatingRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        boolean result = false;
        if (!currentUser.isAnonymousUser()) {
            bookCommentRatingService.saveBookCommentRating(currentUser, commentRatingRequest);
            result = true;
        }
        return new CommentRatingResponse(result);
    }
}
