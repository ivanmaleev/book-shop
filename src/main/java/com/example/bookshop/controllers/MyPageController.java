package com.example.bookshop.controllers;

import com.example.bookshop.entity.Book;
import com.example.bookshop.security.entity.BookstoreUser;
import com.example.bookshop.security.BookstoreUserRegister;
import com.example.bookshop.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы пользователя
 */
@Controller
@Tag(name = "", description = "Контроллер страницы пользователя")
public class MyPageController extends CommonController {

    @Autowired
    private BookService<? extends Book> bookService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @Operation(description = "Получение страницы книг пользователя")
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
