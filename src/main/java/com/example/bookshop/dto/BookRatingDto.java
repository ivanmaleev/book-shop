package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO рейтинга книги
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRatingDto {
    private String bookId;
    private int rating;
    private long ratingCounter;
}
