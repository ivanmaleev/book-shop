package com.example.bookshop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы FAQ
 */
@Controller
@Api(description = "Контроллер страницы FAQ")
public class FaqPageController extends CommonController {

    @ApiOperation("Получение страницы FAQ")
    @ApiResponse(responseCode = "200", description = "Страница FAQ")
    @GetMapping("/faq")
    public String faqPage(Model model) {
        return "/faq";
    }
}
