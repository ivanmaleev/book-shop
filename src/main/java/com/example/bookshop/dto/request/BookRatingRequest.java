package com.example.bookshop.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class BookRatingRequest {
    @NotBlank(message = "Book id must not be null")
    private String bookId;
    @Min(value = 0)
    @Max(value = 5)
    private Integer value;
}
