package com.example.bookshop.controllers;

import com.example.bookshop.service.BookService;
import com.example.bookshop.data.BooksPageDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.Date;

@Controller
@NoArgsConstructor
public class MainPageController {

    @Autowired
    private BookService bookService;

    @GetMapping({"/", "/index.html"})
    public String mainPage(Model model) {
        Date endDate = Date.from(Instant.now());
        Date fromDate = Date.from(Instant.now().minus(3560, ChronoUnit.DAYS));
        model.addAttribute("recommendedBooks", bookService.getPageofRecommendedBooks(0, 6));
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 6, fromDate, endDate));
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 6));
        return "index";
    }
}
