package com.example.bookshop.controller;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.service.BookStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Контроллер смены статуса книг
 */
@Controller
@NoArgsConstructor
@Tag(name = "", description = "Контроллер смены статуса книг")
@Validated
public class BookStatusController {

    @Autowired
    private BookStatusService bookStatusService;

    @Operation(description = "Метод изменения статуса книги")
    @PostMapping("/books/changeBookStatus")
    public void handleChangeBookStatus(@Valid @RequestBody BookCartRequest bookCartRequest,
                                       HttpServletRequest request,
                                       HttpServletResponse response, Model model) {
        bookStatusService.changeBookStatus(bookCartRequest, request, response, model);
    }
}
