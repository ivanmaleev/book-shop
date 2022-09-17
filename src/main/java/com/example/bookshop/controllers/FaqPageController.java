package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FaqPageController {

    @GetMapping("/faq")
    public String faqPage(Model model) {
        return "/faq";
    }
}
