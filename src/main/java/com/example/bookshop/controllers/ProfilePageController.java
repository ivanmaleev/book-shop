package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер страницы профиля пользователя
 */
@Controller
public class ProfilePageController {

    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private CommonService commonService;
    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, true);
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile";
    }
}
