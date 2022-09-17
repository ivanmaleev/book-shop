package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyArchivePageController {

    @GetMapping("/myarchive")
    public String myarchivePage(Model model) {
        return "/myarchive";
    }
}
