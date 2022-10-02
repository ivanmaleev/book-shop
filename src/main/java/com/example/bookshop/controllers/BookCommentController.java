package com.example.bookshop.controllers;

import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.dto.request.BookRatingRequest;
import com.example.bookshop.entity.BookCommentResponse;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookCommentService;
import com.example.bookshop.service.BookRatingService;
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

    @PostMapping("/bookComment")
    @ResponseBody
    public BookCommentResponse rateBook(@RequestBody BookCommentRequest bookCommentRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        if (!currentUser.isAnonymousUser()) {
            bookCommentService.saveBookComment(currentUser, bookCommentRequest);
            return new BookCommentResponse(true);
        }
        return new BookCommentResponse(false);
    }
}
