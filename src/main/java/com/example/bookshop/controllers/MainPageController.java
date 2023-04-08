package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Контроллер главной страницы
 */
@Controller
@NoArgsConstructor
@Api(description = "Контроллер главной страницы")
public class MainPageController {

    @Value("${requests.timout}")
    private Integer timeoutMillis;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @ApiOperation("Получение страницы главной страницы")
    @ApiResponse(responseCode = "200", description = "Главная страница")
    @GetMapping({"/", "/index"})
    public String mainPage(Model model) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<? extends List<? extends Book>> recommendedBooksFuture =
                CompletableFuture.supplyAsync(() -> bookService.getPageOfRecommendedBooks(0, 6));
        CompletableFuture<? extends List<? extends Book>> recentBooksFuture = CompletableFuture.supplyAsync(() -> bookService.getPageOfRecentBooks(0, 6,
                LocalDate.now().minus(3, ChronoUnit.YEARS), LocalDate.now()));
        CompletableFuture<? extends List<? extends Book>> popularBooksFuture = CompletableFuture.supplyAsync(() -> bookService.getPageOfPopularBooks(0, 6));
        model.addAttribute("recommendedBooks", recommendedBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("recentBooks", recentBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("popularBooks", popularBooksFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("topbarActive", new TopBar().setMainActive());
        return "index";
    }
}
