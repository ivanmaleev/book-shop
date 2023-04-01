package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO алфавита
 */
@Data
@AllArgsConstructor
public class AlphabetObject {

    private int id;
    private char letter;
    private String link;
}
