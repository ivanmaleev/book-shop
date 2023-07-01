package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO жанра
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto implements Serializable {

    private long id;
    private String name;
    private int order;
    private int count;
}
