package com.example.bookshop.controllers;

import com.example.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PopularPageController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/popular.html")
    public String popularBooksPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 20));
        return "books/popular";
    }
}
