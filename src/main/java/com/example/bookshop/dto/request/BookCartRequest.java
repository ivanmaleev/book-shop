package com.example.bookshop.dto.request;

import com.example.bookshop.constants.BookStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookCartRequest {
    @NotBlank(message = "Book id must not be null")
    private String bookId;
    @NotBlank
    private BookStatus status;  //KEPT, CART, ARCHIVED, UNLINK
}
