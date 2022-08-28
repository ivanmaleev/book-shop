package com.example.bookshop.controllers;

import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenresController {

    @Value("${locale.default}")
    private String defaultLocale;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookService bookService;

    @GetMapping("/index.html")
    public String getGenresPage(Model model) {
        model.addAttribute("genres", genreService.findGenres(defaultLocale));
        return "genres/index";
    }

    @GetMapping("/slug/{id}")
    public String bookPage(@PathVariable("id") long id, Model model) {
        GenreDto genre = genreService.findGenreById(id, defaultLocale);
        model.addAttribute("booksByGenre", bookService.getBooksByGenre(genre.getName(), 0, 20));
        return "/genres/slug";
    }
}
