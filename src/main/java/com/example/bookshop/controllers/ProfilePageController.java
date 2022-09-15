package com.example.bookshop.controllers;

import com.example.bookshop.security.BookstoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilePageController {

    @Autowired
    private BookstoreUserRegister userRegister;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile";
    }
}
