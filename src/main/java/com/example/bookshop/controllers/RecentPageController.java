package com.example.bookshop.controllers;

import com.example.bookshop.service.BookService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Controller
@NoArgsConstructor
public class RecentPageController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/recent.html")
    public String recentBooksPage(Model model) {
        Date fromDate = Date.from(Instant.now().minus(1068, ChronoUnit.DAYS));
        Date toDate = Date.from(Instant.now());
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20, fromDate, toDate));
        return "/books/recent";
    }
}
