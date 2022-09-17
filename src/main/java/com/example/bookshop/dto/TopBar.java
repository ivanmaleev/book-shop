package com.example.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopBar {
    private final boolean mainActive;
    private final boolean genresActive;
    private final boolean recentActive;
    private final boolean popularActive;
    private final boolean authorsActive;
}
