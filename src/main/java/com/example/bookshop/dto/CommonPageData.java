package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO общих данных страниц
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonPageData {
    private Integer cartBooksCounter;
    private Integer postponedBooksCounter;
    private Integer usersBooksCounter;
}
