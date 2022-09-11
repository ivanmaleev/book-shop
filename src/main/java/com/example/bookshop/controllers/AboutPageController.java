package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutPageController {

    @GetMapping("/about.html")
    public String aboutPage(Model model) {
        return "/about";
    }
}
