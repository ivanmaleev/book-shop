package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Author;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер страницы Авторов
 */
@Controller
@RequestMapping("/authors")
@Api(description = "Контроллер авторов книг")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;

    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

    @ApiOperation("Получение страницы списка авторов")
    @ApiResponse(responseCode = "200", description = "Страница списка авторов")
    @GetMapping({"", "/"})
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        model.addAttribute("topbarActive", new TopBar().setAuthorsActive());
        return "/authors/index";
    }

    @ApiOperation("Получение страницы автора")
    @ApiResponse(responseCode = "200", description = "Страница автора")
    @GetMapping("/slug/{id}")
    public String authorPage(@PathVariable("id") long id, Model model) throws Exception {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        model.addAttribute("authorBooks", bookService.getBooksByAuthor(author, 0, 20));
        return "/authors/slug";
    }
}
