package com.example.bookshop.controller;

import com.example.bookshop.dto.request.BookCommentRequest;
import com.example.bookshop.dto.request.CommentRatingRequest;
import com.example.bookshop.dto.response.BookCommentResponse;
import com.example.bookshop.dto.response.CommentRatingResponse;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookCommentRatingService;
import com.example.bookshop.service.BookCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

/**
 * Контроллер комментариев книг
 */
@RestController
@Tag(name = "", description = "Контроллер комментариев книг")
@Validated
public class BookCommentController {
    @Autowired
    private BookCommentService bookCommentService;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private BookCommentRatingService bookCommentRatingService;

    @Operation(description = "Сохранение комментария книги")
    @ApiResponse(responseCode = "200", description = "Результат сохранения комментария книги")
    @PostMapping("/bookComment")
    public BookCommentResponse rateBook(@Valid @RequestBody BookCommentRequest bookCommentRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        boolean result = false;
        if (!currentUser.isAnonymousUser()) {
            CompletableFuture.runAsync(() -> bookCommentService.saveBookComment(currentUser, bookCommentRequest));
            result = true;
        }
        return new BookCommentResponse(result);
    }

    @Operation(description = "Сохранение рейтинга комментария книги")
    @ApiResponse(responseCode = "200", description = "Результат сохранения рейтинга комментария книги")
    @PostMapping("/rateBookComment")
    public CommentRatingResponse rateBook(@Valid @RequestBody CommentRatingRequest commentRatingRequest) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        boolean result = false;
        if (!currentUser.isAnonymousUser()) {
            CompletableFuture.runAsync(() -> bookCommentRatingService.saveBookCommentRating(currentUser, commentRatingRequest));
            result = true;
        }
        return new CommentRatingResponse(result);
    }
}
