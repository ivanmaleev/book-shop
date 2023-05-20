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
 * Контроллер страницы архива пользователя
 */
@Controller
@Api(description = "Контроллер страницы архива пользователя")
public class MyArchivePageController extends CommonController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @ApiOperation("Получение страницы архивных книг пользователя")
    @ApiResponse(responseCode = "200", description = "Страница архивных книг")
    @GetMapping("/myarchive")
    public String myArchivePage(Model model) {
        BookstoreUser currentUser = (BookstoreUser) userRegister.getCurrentUser();
        model.addAttribute("usersArchivedBooks", bookService.findUsersBooks(currentUser.getId(), true));
        return "/myarchive";
    }

    @Override
    public boolean isUsersPage() {
        return true;
    }
}
