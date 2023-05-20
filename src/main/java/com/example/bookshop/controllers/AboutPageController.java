package com.example.bookshop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы About
 */
@Controller
@Api(description = "Контроллер страницы 'О компании'")
public class AboutPageController extends CommonController {

    @ApiOperation("Получение страницы информации о компании")
    @ApiResponse(responseCode = "200", description = "Страница информации о компнии")
    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "/about";
    }
}
