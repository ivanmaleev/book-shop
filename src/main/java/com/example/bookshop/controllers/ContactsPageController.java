package com.example.bookshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactsPageController {

    @GetMapping("/contacts.html")
    public String contactsPage(Model model) {
        return "/contacts";
    }
}