package com.example.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы контактов
 */
@Controller
@Tag(name = "", description = "Контроллер страницы контактов")
public class ContactsPageController extends CommonController {

    @Operation(description = "Получение страницы контактоы")
    @ApiResponse(responseCode = "200", description = "Страница контактов")
    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        return "/contacts";
    }
}
