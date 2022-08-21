package com.example.bookshop.controllers;

import com.example.bookshop.data.SearchWordDto;
import com.example.bookshop.errs.EmptySearchException;
import com.example.bookshop.service.BookService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@NoArgsConstructor
public class SearchPageController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/search", "/search/{SearchWord}"})
    public String getSearchResult(@PathVariable(value = "SearchWord", required = false) String searchWord,
                                  Model model) throws EmptySearchException {
        if (searchWord != null) {

            model.addAttribute("searchWordDto", new SearchWordDto(searchWord));
            model.addAttribute("searchResultBooks",
                    bookService.getPageOfGoogleBooksApiSearchResult(searchWord, 0, 6));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }

    }
}
