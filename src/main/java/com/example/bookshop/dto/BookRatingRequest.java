package com.example.bookshop.dto;

import lombok.Data;

@Data
public class BookRatingRequest {
    private String bookId;
    private Integer value;
}
