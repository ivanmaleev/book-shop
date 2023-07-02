package com.example.bookshop.controller;

import com.example.bookshop.security.BookstoreUserRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы профиля пользователя
 */
@Controller
@Tag(name = "", description = "Контроллер страницы профиля пользователя")
public class ProfilePageController extends CommonController {

    @Autowired
    private BookstoreUserRegister userRegister;

    @Operation(description = "Получение страницы профиля пользователя")
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
