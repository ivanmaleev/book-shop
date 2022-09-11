package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostponedPageController {

    @GetMapping("/postponed.html")
    public String postponedPage(Model model) {
        return "/postponed";
    }
}
