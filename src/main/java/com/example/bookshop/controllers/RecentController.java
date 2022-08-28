package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecentController {

    @GetMapping("/books/recent.html")
    public String getGenresPage() {
        return "books/recent";
    }
}
