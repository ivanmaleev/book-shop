package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер страницы пользователя
 */
@Controller
public class MyPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private CommonService commonService;
    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, true);
    }
    @GetMapping("/my")
    public String myPage(Model model) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        model.addAttribute("usersBooks", bookService.findUsersBooks(currentUser.getId(), false));
        return "/my";
    }
}
