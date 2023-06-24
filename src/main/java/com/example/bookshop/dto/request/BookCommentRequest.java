package com.example.bookshop.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookCommentRequest {
    @NotBlank(message = "Book id must not be null")
    private String bookId;
    @NotBlank(message = "Comment must not be empty")
    private String text;
}
