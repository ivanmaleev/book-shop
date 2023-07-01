package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.service.PostponedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Контроллер страницы отложенных книг пользователя
 */
@Controller
@Tag(name = "", description = "Контроллер страницы отложенных книг пользователя")
public class PostponedPageController extends CommonController {

    @Autowired
    private PostponedService postponedService;

    @Operation(description = "Получение страницы отложенных книг")
    @ApiResponse(responseCode = "200", description = "Страница отложенных книг")
    @GetMapping("/books/postponed")
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model) {
        if (StringUtils.isBlank(postponedContents)) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            Collection<BookDto> postponedBooks = postponedService.getPostponedBooks(postponedContents);
            model.addAttribute("isPostponedEmpty", false);
            model.addAttribute("booksPostponed", postponedBooks);
            model.addAttribute("bookIdsPostponed", postponedBooks
                    .stream()
                    .map(BookDto::getSlug)
                    .collect(Collectors.toList()));
        }
        return "/postponed";
    }
}
