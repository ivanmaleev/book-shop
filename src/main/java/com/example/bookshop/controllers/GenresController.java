package com.example.bookshop.controllers;

import com.example.bookshop.constants.Langs;
import com.example.bookshop.dto.CommonPageData;
import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.dto.TopBar;
import com.example.bookshop.service.BookService;
import com.example.bookshop.service.CommonService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер страницы жанров
 */
@Controller
@NoArgsConstructor
@RequestMapping("/genres")
@Api(description = "Контроллер страницы жанров")
public class GenresController {

    @Value("${locale.default}")
    private String defaultLocale;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommonService commonService;
    @ModelAttribute("commonData")
    public CommonPageData commonPageData(HttpServletRequest request) {
        return commonService.getCommonPageData(request, false);
    }

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
    public String bookPage(@PathVariable("id") long genreId, Model model) {
        GenreDto genreDto = genreService.findGenreById(genreId, Langs.RU);
        model.addAttribute("genre", genreDto);
        model.addAttribute("booksByGenre", bookService.getBooksByGenreId(genreDto.getId(), 0, 20));
        return "/genres/slug";
    }
}
