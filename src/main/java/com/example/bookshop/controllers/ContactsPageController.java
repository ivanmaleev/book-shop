package com.example.bookshop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы контактов
 */
@Controller
@Api(description = "Контроллер страницы контактов")
public class ContactsPageController extends CommonController {

    @ApiOperation("Получение страницы контактоы")
    @ApiResponse(responseCode = "200", description = "Страница контактов")
    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        return "/contacts";
    }
}
