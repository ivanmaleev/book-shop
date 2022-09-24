package com.example.bookshop.controllers;

import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.AuthorService;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
import com.example.bookshop.service.impl.AlphabetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;
    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request);
    }

    @GetMapping({"", "/"})
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        model.addAttribute("topbarActive", new TopBar(false, false, false, false, true));
        return "/authors/index";
    }

    @GetMapping("/slug/{id}")
    public String authorPage(@PathVariable("id") long id, Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        model.addAttribute("authorBooks", bookService.getBooksByAuthor(author, 0, 20));
        return "/authors/slug";
    }
}
