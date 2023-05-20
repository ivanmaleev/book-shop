package com.example.bookshop.controllers;

import com.example.bookshop.security.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы пользователя
 */
@Controller
@Api(description = "Контроллер страницы пользователя")
public class MyPageController extends CommonController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @ApiOperation("Получение страницы книг пользователя")
    @ApiResponse(responseCode = "200", description = "Страница книг пользователя")
    @GetMapping("/my")
    public String myPage(Model model) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        model.addAttribute("usersBooks", bookService.findUsersBooks(currentUser.getId(), false));
        return "/my";
    }

    @Override
    public boolean isUsersPage() {
        return true;
    }
}
