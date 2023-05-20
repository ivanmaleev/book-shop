package com.example.bookshop.controllers;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.entity.Book;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.GenreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Контроллер страницы жанров
 */
@Controller
@NoArgsConstructor
@RequestMapping("/genres")
@Api(description = "Контроллер страницы жанров")
public class GenresController extends CommonController {

    @Value("${requests.timout}")
    private Integer timeoutMillis;
    @Value("${locale.default}")
    private String defaultLocale;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookService bookService;

    @ApiOperation("Получение страницы жанров")
    @ApiResponse(responseCode = "200", description = "Страница жанров")
    @GetMapping({"", "/"})
    public String getGenresPage(Model model) {
        model.addAttribute("genres", genreService.findGenres(defaultLocale));
        model.addAttribute("topbarActive", new TopBar().setGenresActive());
        return "genres/index";
    }

    @ApiOperation("Получение страницы жанра")
    @ApiResponse(responseCode = "200", description = "Страница жанра")
    @GetMapping("/slug/{id}")
    public String bookPage(@PathVariable("id") long genreId, Model model) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<GenreDto> genreFuture = CompletableFuture.supplyAsync(() -> genreService.findGenreById(genreId, Langs.RU));
        CompletableFuture<? extends List<? extends Book>> booksByGenreFuture =
                CompletableFuture.supplyAsync(() -> bookService.getBooksByGenreId(genreId, 0, 20));

        model.addAttribute("genre", genreFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        model.addAttribute("booksByGenre", booksByGenreFuture.get(timeoutMillis, TimeUnit.MILLISECONDS));
        return "/genres/slug";
    }
}
