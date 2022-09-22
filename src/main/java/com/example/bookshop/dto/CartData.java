package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartData {
    private Integer sumPrice;
    private Integer sumOldPrice;
}
