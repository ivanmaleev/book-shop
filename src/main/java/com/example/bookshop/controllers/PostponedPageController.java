package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.BookRatingDto;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookRatingService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PostponedPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BookRatingService bookRatingService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request);
    }

    @GetMapping("/books/postponed")
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            postponedContents = postponedContents.startsWith("/") ? postponedContents.substring(1) : postponedContents;
            postponedContents = postponedContents.endsWith("/") ? postponedContents.substring(0, postponedContents.length() - 1) :
                    postponedContents;
            String[] cookieSlugs = postponedContents.split("/");
            List<Book> books = Arrays.stream(cookieSlugs)
                    .map(slug -> bookService.getBook(slug))
                    .collect(Collectors.toList());
            Map<String, List<BookRatingDto>> bookRatings = bookRatingService.getBooksRating(books
                            .stream()
                            .map(Book::getSlug)
                            .collect(Collectors.toList()))
                    .stream()
                    .collect(Collectors.groupingBy(BookRatingDto::getBookId));
            List<BookDto> bookDtos = books
                    .stream()
                    .map(book -> {
                        BookDto bookDto = new BookDto(book);
                        List<BookRatingDto> bookRatingDtos = bookRatings.get(book.getSlug());
                        if (bookRatingDtos != null && !bookRatingDtos.isEmpty()) {
                            bookDto.setRating(bookRatingDtos.get(0).getRating());
                        }
                        return bookDto;
                    }).collect(Collectors.toList());
            model.addAttribute("bookPostponed", bookDtos);
        }
        return "/postponed";
    }
}
