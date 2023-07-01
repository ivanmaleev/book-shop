package com.example.bookshop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы About
 */
@Controller
@Tag(name = "About controller", description = "Контроллер страницы 'О компании'")
public class AboutPageController extends CommonController {

    @Operation(description = "Получение страницы информации о компании")
    @ApiResponse(responseCode = "200", description = "Страница информации о компнии")
    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "/about";
    }
}
