package com.example.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер страницы FAQ
 */
@Controller
@Tag(name = "", description = "Контроллер страницы FAQ")
public class FaqPageController extends CommonController {

    @Operation(description = "Получение страницы FAQ")
    @ApiResponse(responseCode = "200", description = "Страница FAQ")
    @GetMapping("/faq")
    public String faqPage(Model model) {
        return "/faq";
    }
}
