package com.example.bookshop.controller;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Контроллер главной страницы
 */
@Controller
@NoArgsConstructor
@Tag(name = "", description = "Контроллер главной страницы")
public class MainPageController extends CommonController {

    @Value("${requests.timout}")
    private Integer timeoutMillis;
    @Autowired
    private BookService bookService;

    @Operation(description = "Получение страницы главной страницы")
    @ApiResponse(responseCode = "200", description = "Главная страница")
    @GetMapping({"/", "/index"})
    public String mainPage(Model model) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<? extends Collection<BookDto>> recommendedBooksFuture =
                CompletableFuture.supplyAsync(() -> bookService.getPageOfRecommendedBooks(0, 6));
        CompletableFuture<? extends Collection<BookDto>> recentBooksFuture = CompletableFuture.supplyAsync(() -> bookService.getPageOfRecentBooks(0, 6,
                LocalDate.now().minus(3, ChronoUnit.YEARS), LocalDate.now()));
        CompletableFuture<? extends Collection<BookDto>> popularBooksFuture = CompletableFuture.supplyAsync(() -> bookService.getPageOfPopularBooks(0, 6));
        model.addAttribute("recommendedBooks", recommendedBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("recentBooks", recentBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("popularBooks", popularBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("topbarActive", new TopBar().setMainActive());
        return "index";
    }
}
