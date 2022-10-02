package com.example.bookshop.dto.request;

import lombok.Data;

@Data
public class BookCommentRequest {
    private String bookId;
    private String text;
}
