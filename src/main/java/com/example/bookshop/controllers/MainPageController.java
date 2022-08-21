package com.example.bookshop.controllers;

import com.example.bookshop.data.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.data.BooksPageDto;
import com.example.bookshop.data.SearchWordDto;
import com.example.bookshop.errs.EmptySearchException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@NoArgsConstructor
public class MainPageController {

    @Autowired
    private BookService bookService;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageofRecommendedBooks(0, 6);
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageofRecommendedBooks(offset, limit));
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                           @RequestParam(value = "limit", defaultValue = "6") Integer limit) throws ParseException {
        Date endDate = Date.from(Instant.now());
        Date fromDate = Date.from(Instant.now().minus(3560, ChronoUnit.DAYS));
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit, fromDate, endDate));
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {

            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults",
                    bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }

    }

//    @GetMapping("/search/page/{searchWord}")
//    @ResponseBody
//    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
//                                          @RequestParam("limit") Integer limit,
//                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
//        return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit));
//    }

    @GetMapping({"/", "/index.html"})
    public String mainPage(Model model) {

        Date endDate = Date.from(Instant.now());
        Date fromDate = Date.from(Instant.now().minus(3560, ChronoUnit.DAYS));
        model.addAttribute("recentBooks", bookService.getPageOfRecentBooks(0, 6, fromDate, endDate));
        model.addAttribute("recommended", bookService.getPageofRecommendedBooks(0, 6));
        return "index";
    }

    private Date parseDate(String dateText) {
        if (dateText != null && !dateText.isEmpty()) {
            try {
                return sdf.parse(dateText);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return Date.from(Instant.now());
    }
}
