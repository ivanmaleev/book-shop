package com.example.bookshop.dto;

import com.example.bookshop.constants.BookStatus;
import lombok.Data;

@Data
public class BookCartRequest {
    private String bookId;
    private BookStatus status;  //KEPT, CART, ARCHIVED, UNLINK
}
