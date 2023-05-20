package com.example.bookshop.controllers;

import com.example.bookshop.dto.TopBar;
import com.example.bookshop.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Контроллер страницы новых книг
 */
@Controller
@NoArgsConstructor
@RequestMapping("/recent")
@Api(description = "Контроллер страницы новых книг")
public class RecentPageController extends CommonController {

    @Autowired
    private BookService bookService;

    @ApiOperation("Получение страницы новых книг")
    @ApiResponse(responseCode = "200", description = "Страница новых книг")
    @GetMapping({"", "/"})
    public String recentBooksPage(Model model) {
        LocalDate fromDate = LocalDate.now().minus(3, ChronoUnit.YEARS);
        LocalDate toDate = LocalDate.now();
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 20, fromDate, toDate));
        model.addAttribute("topbarActive", new TopBar().setRecentActive());
        return "/books/recent";
    }
}
