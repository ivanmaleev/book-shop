package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PopularController {

    @GetMapping("/books/popular.html")
    public String getGenresPage() {
        return "books/popular";
    }
}
