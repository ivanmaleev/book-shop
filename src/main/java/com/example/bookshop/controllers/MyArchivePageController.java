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
 * Контроллер страницы архива пользователя
 */
@Controller
@Tag(name = "", description = "Контроллер страницы архива пользователя")
public class MyArchivePageController extends CommonController {

    @Autowired
    private BookService<? extends Book> bookService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @Operation(description = "Получение страницы архивных книг пользователя")
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
