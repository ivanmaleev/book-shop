package com.example.bookshop.controller;

import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер страницы популярных книг
 */
@Controller
@NoArgsConstructor
@RequestMapping("/popular")
@Tag(name = "", description = "Контроллер страницы популярных книг")
public class PopularPageController extends CommonController {

    @Autowired
    private BookService bookService;

    @Operation(description = "Получение страницы популярных книг")
    @ApiResponse(responseCode = "200", description = "Страница популярных книг")
    @GetMapping({"", "/"})
    public String popularBooksPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 20));
        model.addAttribute("topbarActive", new TopBar().setPopularActive());
        return "books/popular";
    }
}
