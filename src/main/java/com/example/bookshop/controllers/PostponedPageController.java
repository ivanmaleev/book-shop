package com.example.bookshop.controllers;

import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class PostponedPageController {

    @Autowired
    private BookService bookService;

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
            model.addAttribute("bookPostponed", Arrays.stream(cookieSlugs)
                    .map(slug -> bookService.getBook(slug))
                    .collect(Collectors.toList()));
        }
        return "/postponed";
    }
}
