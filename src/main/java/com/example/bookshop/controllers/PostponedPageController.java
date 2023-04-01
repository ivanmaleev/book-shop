package com.example.bookshop.controllers;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.PostponedService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер страницы отложенных книг пользователя
 */
@Controller
public class PostponedPageController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private PostponedService postponedService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @GetMapping("/books/postponed")
    public String postponedPage(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                Model model) {
        if (StringUtils.isBlank(postponedContents)) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            List<BookDto> postponedBooks = postponedService.getPostponedBooks(postponedContents);
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
