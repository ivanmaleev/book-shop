package com.example.bookshop.controllers;

import com.example.bookshop.security.BookstoreUserRegister;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы профиля пользователя
 */
@Controller
@Api(description = "Контроллер страницы профиля пользователя")
public class ProfilePageController extends CommonController {

    @Autowired
    private BookstoreUserRegister userRegister;

    @ApiOperation("Получение страницы профиля пользователя")
    @ApiResponse(responseCode = "200", description = "Страница профиля пользователя")
    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile";
    }

    @Override
    public boolean isUsersPage() {
        return true;
    }
}
