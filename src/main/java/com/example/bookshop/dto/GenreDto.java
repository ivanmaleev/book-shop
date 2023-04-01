package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO жанра
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    private long id;
    private String name;
    private int order;
    private int count;
}
