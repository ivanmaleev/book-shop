package com.example.bookshop.dto.request;

import lombok.Data;

@Data
public class BookRatingRequest {
    private String bookId;
    private Integer value;
}
