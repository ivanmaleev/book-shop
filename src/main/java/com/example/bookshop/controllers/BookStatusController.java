package com.example.bookshop.controllers;

import com.example.bookshop.dto.request.BookCartRequest;
import com.example.bookshop.service.BookStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Контроллер смены статуса книг
 */
@Controller
@NoArgsConstructor
@Api(description = "Контроллер смены статуса книг")
public class BookStatusController {

    @Autowired
    private BookStatusService bookStatusService;

    @ApiOperation("Метод изменения статуса книги")
    @PostMapping("/books/changeBookStatus")
    public void handleChangeBookStatus(@RequestBody BookCartRequest bookCartRequest,
                                       HttpServletRequest request,
                                       HttpServletResponse response, Model model) {
        bookStatusService.changeBookStatus(bookCartRequest, request, response, model);
    }
}
